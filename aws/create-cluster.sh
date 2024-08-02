#!/bin/bash

set -e

# Configuraciones iniciales
VPC_CIDR="10.0.0.0/16"
SUBNET1_PUBLIC_CIDR="10.0.1.0/24"
SUBNET2_PUBLIC_CIDR="10.0.2.0/24"
SUBNET1_PRIVATE_CIDR="10.0.3.0/24"
SUBNET2_PRIVATE_CIDR="10.0.4.0/24"
REGION="us-east-1"
ROLE_NAME="eks-cluster-role"
POLICY_DOCUMENT="eks-trust-policy.json"
POLICY_NAME="PassRolePolicy"
USER_NAME="dwinpaez"
CLUSTER_NAME="casapp-eks-cluster"

# Crear la política JSON para iam:PassRole y permisos EKS
cat > pass-role-policy.json << EOL
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "iam:PassRole",
      "Resource": "arn:aws:iam::123456789012:role/$ROLE_NAME"
    },
    {
      "Effect": "Allow",
      "Action": [
        "eks:CreateCluster",
        "eks:DescribeCluster",
        "eks:ListClusters",
        "ec2:DescribeSubnets",
        "ec2:DescribeVpcs",
        "ec2:DescribeSecurityGroups"
      ],
      "Resource": "*"
    }
  ]
}
EOL

# Verificar si la política ya existe
POLICY_ARN=$(aws iam list-policies --query "Policies[?PolicyName=='$POLICY_NAME'].Arn" --output text)
if [ -z "$POLICY_ARN" ]; then
    # Crear la política en IAM
    POLICY_ARN=$(aws iam create-policy --policy-name $POLICY_NAME --policy-document file://pass-role-policy.json --query 'Policy.Arn' --output text)
    echo "Created policy: $POLICY_ARN"
else
    echo "Policy already exists: $POLICY_ARN"
fi

# Verificar si la política ya está adjunta al usuario
ATTACHED_POLICIES=$(aws iam list-attached-user-policies --user-name $USER_NAME --query 'AttachedPolicies[*].PolicyArn' --output text)
if ! echo "$ATTACHED_POLICIES" | grep -q "$POLICY_ARN"; then
    # Adjuntar la política al usuario
    aws iam attach-user-policy --user-name $USER_NAME --policy-arn $POLICY_ARN
    echo "Attached policy $POLICY_ARN to user $USER_NAME"
else
    echo "Policy $POLICY_ARN is already attached to user $USER_NAME"
fi

# Crear la política JSON para la política de confianza del rol de IAM
cat > eks-trust-policy.json << EOL
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "eks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    },
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::730335182446:user/$USER_NAME"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
EOL

# Verificar si el rol de IAM ya existe
ROLE_ARN=$(aws iam get-role --role-name $ROLE_NAME --query 'Role.Arn' --output text 2>/dev/null || echo "None")

if [ "$ROLE_ARN" = "None" ]; then
    # Crear el rol si no existe
    ROLE_ARN=$(aws iam create-role --role-name $ROLE_NAME --assume-role-policy-document file://$POLICY_DOCUMENT --query 'Role.Arn' --output text)
    echo "Created IAM Role: $ROLE_ARN"
else
    echo "Found IAM Role: $ROLE_ARN"
fi

# Actualizar la política de confianza del rol de IAM
aws iam update-assume-role-policy --role-name $ROLE_NAME --policy-document file://eks-trust-policy.json

# Verificar el usuario actual
echo "Verificando el usuario actual:"
aws sts get-caller-identity

# Crear o encontrar VPC
VPC_ID=$(aws ec2 describe-vpcs --filters "Name=cidr,Values=$VPC_CIDR" --query 'Vpcs[0].VpcId' --output text)
if [ "$VPC_ID" = "None" ]; then
    VPC_ID=$(aws ec2 create-vpc --cidr-block $VPC_CIDR --query 'Vpc.VpcId' --output text)
    echo "Created VPC: $VPC_ID"
else
    echo "Found VPC: $VPC_ID"
fi

# Crear o encontrar subredes públicas
SUBNET1_PUBLIC_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VPC_ID" "Name=cidr-block,Values=$SUBNET1_PUBLIC_CIDR" --query 'Subnets[0].SubnetId' --output text)
if [ "$SUBNET1_PUBLIC_ID" = "None" ]; then
    SUBNET1_PUBLIC_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block $SUBNET1_PUBLIC_CIDR --availability-zone $REGION"a" --query 'Subnet.SubnetId' --output text)
    echo "Created public subnet 1: $SUBNET1_PUBLIC_ID"
else
    echo "Found public subnet 1: $SUBNET1_PUBLIC_ID"
fi

SUBNET2_PUBLIC_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VPC_ID" "Name=cidr-block,Values=$SUBNET2_PUBLIC_CIDR" --query 'Subnets[0].SubnetId' --output text)
if [ "$SUBNET2_PUBLIC_ID" = "None" ]; then
    SUBNET2_PUBLIC_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block $SUBNET2_PUBLIC_CIDR --availability-zone $REGION"b" --query 'Subnet.SubnetId' --output text)
    echo "Created public subnet 2: $SUBNET2_PUBLIC_ID"
else
    echo "Found public subnet 2: $SUBNET2_PUBLIC_ID"
fi

# Crear o encontrar subredes privadas
SUBNET1_PRIVATE_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VPC_ID" "Name=cidr-block,Values=$SUBNET1_PRIVATE_CIDR" --query 'Subnets[0].SubnetId' --output text)
if [ "$SUBNET1_PRIVATE_ID" = "None" ]; then
    SUBNET1_PRIVATE_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block $SUBNET1_PRIVATE_CIDR --availability-zone $REGION"a" --query 'Subnet.SubnetId' --output text)
    echo "Created private subnet 1: $SUBNET1_PRIVATE_ID"
else
    echo "Found private subnet 1: $SUBNET1_PRIVATE_ID"
fi

SUBNET2_PRIVATE_ID=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=$VPC_ID" "Name=cidr-block,Values=$SUBNET2_PRIVATE_CIDR" --query 'Subnets[0].SubnetId' --output text)
if [ "$SUBNET2_PRIVATE_ID" = "None" ]; then
    SUBNET2_PRIVATE_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block $SUBNET2_PRIVATE_CIDR --availability-zone $REGION"b" --query 'Subnet.SubnetId' --output text)
    echo "Created private subnet 2: $SUBNET2_PRIVATE_ID"
else
    echo "Found private subnet 2: $SUBNET2_PRIVATE_ID"
fi

# Crear o encontrar Gateway de Internet
IGW_ID=$(aws ec2 describe-internet-gateways --filters "Name=attachment.vpc-id,Values=$VPC_ID" --query 'InternetGateways[0].InternetGatewayId' --output text)
if [ "$IGW_ID" = "None" ]; then
    IGW_ID=$(aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text)
    aws ec2 attach-internet-gateway --vpc-id $VPC_ID --internet-gateway-id $IGW_ID
    echo "Created and attached Internet Gateway: $IGW_ID"
else
    echo "Found Internet Gateway: $IGW_ID"
fi

# Crear o encontrar Tabla de Rutas Pública
RTB_PUBLIC_ID=$(aws ec2 describe-route-tables --filters "Name=vpc-id,Values=$VPC_ID" "Name=association.main,Values=false" --query 'RouteTables[0].RouteTableId' --output text)
if [ "$RTB_PUBLIC_ID" = "None" ]; then
    RTB_PUBLIC_ID=$(aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text)
    aws ec2 create-route --route-table-id $RTB_PUBLIC_ID --destination-cidr-block 0.0.0.0/0 --gateway-id $IGW_ID
    echo "Created Route Table for public subnets: $RTB_PUBLIC_ID"
else
    echo "Found Route Table for public subnets: $RTB_PUBLIC_ID"
fi

aws ec2 associate-route-table --subnet-id $SUBNET1_PUBLIC_ID --route-table-id $RTB_PUBLIC_ID
aws ec2 associate-route-table --subnet-id $SUBNET2_PUBLIC_ID --route-table-id $RTB_PUBLIC_ID

# Habilitar IPs Públicas en Subredes Públicas
aws ec2 modify-subnet-attribute --subnet-id $SUBNET1_PUBLIC_ID --map-public-ip-on-launch
aws ec2 modify-subnet-attribute --subnet-id $SUBNET2_PUBLIC_ID --map-public-ip-on-launch

# Crear NAT Gateway
NAT_GW_ID=$(aws ec2 describe-nat-gateways --filter "Name=vpc-id,Values=$VPC_ID" --query 'NatGateways[0].NatGatewayId' --output text)
if [ "$NAT_GW_ID" = "None" ]; then
    EIP_ALLOC_ID=$(aws ec2 allocate-address --query 'AllocationId' --output text)
    NAT_GW_ID=$(aws ec2 create-nat-gateway --subnet-id $SUBNET1_PUBLIC_ID --allocation-id $EIP_ALLOC_ID --query 'NatGateway.NatGatewayId' --output text)
    echo "Created NAT Gateway: $NAT_GW_ID"
else
    echo "Found NAT Gateway: $NAT_GW_ID"
fi

# Esperar a que el NAT Gateway esté disponible
STATE="pending"
while [ "$STATE" != "available" ]; do
    STATE=$(aws ec2 describe-nat-gateways --nat-gateway-ids $NAT_GW_ID --query 'NatGateways[0].State' --output text)
    echo "Waiting for NAT Gateway to be available. Current state: $STATE"
    sleep 10
done

# Crear o encontrar Tabla de Rutas Privada
RTB_PRIVATE_ID=$(aws ec2 describe-route-tables --filters "Name=vpc-id,Values=$VPC_ID" "Name=association.main,Values=false" --query 'RouteTables[1].RouteTableId' --output text)
if [ "$RTB_PRIVATE_ID" = "None" ]; then
    RTB_PRIVATE_ID=$(aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text)
    aws ec2 create-route --route-table-id $RTB_PRIVATE_ID --destination-cidr-block 0.0.0.0/0 --nat-gateway-id $NAT_GW_ID
    echo "Created Route Table for private subnets: $RTB_PRIVATE_ID"
else
    echo "Found Route Table for private subnets: $RTB_PRIVATE_ID"
fi

aws ec2 associate-route-table --subnet-id $SUBNET1_PRIVATE_ID --route-table-id $RTB_PRIVATE_ID
aws ec2 associate-route-table --subnet-id $SUBNET2_PRIVATE_ID --route-table-id $RTB_PRIVATE_ID

# Crear o encontrar Grupo de Seguridad
SG_ID=$(aws ec2 describe-security-groups --filters "Name=vpc-id,Values=$VPC_ID" "Name=group-name,Values=my-eks-sg" --query 'SecurityGroups[0].GroupId' --output text)
if [ "$SG_ID" = "None" ]; then
    SG_ID=$(aws ec2 create-security-group --group-name my-eks-sg --description "EKS security group" --vpc-id $VPC_ID --query 'GroupId' --output text)
    echo "Created Security Group: $SG_ID"
else
    echo "Found Security Group: $SG_ID"
fi

# Agregar Reglas al Grupo de Seguridad
authorize_security_group_ingress() {
    local group_id=$1
    local protocol=$2
    local port=$3
    local cidr=$4

    existing_rule=$(aws ec2 describe-security-groups --group-ids $group_id --query "SecurityGroups[0].IpPermissions[?FromPort==\`$port\` && ToPort==\`$port\` && IpProtocol==\`$protocol\` && IpRanges[0].CidrIp==\`$cidr\`]" --output text)

    if [ -z "$existing_rule" ]; then
        aws ec2 authorize-security-group-ingress --group-id $group_id --protocol $protocol --port $port --cidr $cidr
        echo "Authorized ingress $protocol on port $port from $cidr for group $group_id"
    else
        echo "Ingress $protocol on port $port from $cidr already exists for group $group_id"
    fi
}

authorize_security_group_ingress $SG_ID "tcp" 443 "0.0.0.0/0"
authorize_security_group_ingress $SG_ID "tcp" 80 "0.0.0.0/0"

# Adjuntar la política de EKS al rol (si no está adjunta)
POLICY_ARN="arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
ATTACHED_POLICIES=$(aws iam list-attached-role-policies --role-name $ROLE_NAME --query 'AttachedPolicies[*].PolicyArn' --output text)

if ! echo "$ATTACHED_POLICIES" | grep -q "$POLICY_ARN"; then
    aws iam attach-role-policy --role-name $ROLE_NAME --policy-arn $POLICY_ARN
    echo "Attached policy $POLICY_ARN to IAM Role: $ROLE_NAME"
else
    echo "Policy $POLICY_ARN is already attached to IAM Role: $ROLE_NAME"
fi

# Crear el Clúster EKS
aws eks create-cluster \
  --name $CLUSTER_NAME \
  --role-arn $ROLE_ARN \
  --resources-vpc-config subnetIds=$SUBNET1_PUBLIC_ID,$SUBNET2_PUBLIC_ID,$SUBNET1_PRIVATE_ID,$SUBNET2_PRIVATE_ID,securityGroupIds=$SG_ID

# Esperar a que el clúster EKS esté activo
CLUSTER_STATUS="CREATING"
while [ "$CLUSTER_STATUS" != "ACTIVE" ]; do
    CLUSTER_STATUS=$(aws eks describe-cluster --name $CLUSTER_NAME --query 'cluster.status' --output text)
    echo "Waiting for EKS cluster to be ACTIVE. Current status: $CLUSTER_STATUS"
    sleep 30
done

echo "EKS Cluster is now ACTIVE and fully operational."
