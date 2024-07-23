# Crear Cluster Role

aws iam create-role --role-name eks-cluster-role --assume-role-policy-document file://eks-trust-policy.json
aws iam attach-role-policy --role-name eks-cluster-role --policy-arn arn:aws:iam::aws:policy/AmazonEKSClusterPolicy

Para configurar una VPC en AWS con subredes públicas y privadas, necesitas crear una VPC, subredes públicas y privadas, un gateway de Internet, una tabla de rutas públicas y privadas, y un NAT Gateway para permitir que las subredes privadas accedan a Internet. A continuación, te explico cómo hacerlo usando la AWS CLI:

# Crear la VPC
VPC_ID=$(aws ec2 create-vpc --cidr-block 10.0.0.0/16 --query 'Vpc.VpcId' --output text)

# Crear Subredes Públicas
SUBNET1_PUBLIC_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.1.0/24 --availability-zone us-west-2a --query 'Subnet.SubnetId' --output text)
SUBNET2_PUBLIC_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.2.0/24 --availability-zone us-west-2b --query 'Subnet.SubnetId' --output text)

# Crear Subredes Privadas
SUBNET1_PRIVATE_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.3.0/24 --availability-zone us-west-2a --query 'Subnet.SubnetId' --output text)
SUBNET2_PRIVATE_ID=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.4.0/24 --availability-zone us-west-2b --query 'Subnet.SubnetId' --output text)

# Crear y adjuntar Gateway de Internet
IGW_ID=$(aws ec2 create-internet-gateway --query 'InternetGateway.InternetGatewayId' --output text)
aws ec2 attach-internet-gateway --vpc-id $VPC_ID --internet-gateway-id $IGW_ID

# Crear y configurar Tabla de Rutas Pública
RTB_PUBLIC_ID=$(aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text)
aws ec2 create-route --route-table-id $RTB_PUBLIC_ID --destination-cidr-block 0.0.0.0/0 --gateway-id $IGW_ID
aws ec2 associate-route-table --subnet-id $SUBNET1_PUBLIC_ID --route-table-id $RTB_PUBLIC_ID
aws ec2 associate-route-table --subnet-id $SUBNET2_PUBLIC_ID --route-table-id $RTB_PUBLIC_ID

# Habilitar IPs Públicas en Subredes Públicas
aws ec2 modify-subnet-attribute --subnet-id $SUBNET1_PUBLIC_ID --map-public-ip-on-launch
aws ec2 modify-subnet-attribute --subnet-id $SUBNET2_PUBLIC_ID --map-public-ip-on-launch

# Crear NAT Gateway
EIP_ALLOC_ID=$(aws ec2 allocate-address --query 'AllocationId' --output text)
NAT_GW_ID=$(aws ec2 create-nat-gateway --subnet-id $SUBNET1_PUBLIC_ID --allocation-id $EIP_ALLOC_ID --query 'NatGateway.NatGatewayId' --output text)

# Crear y configurar Tabla de Rutas Privada
RTB_PRIVATE_ID=$(aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text)
aws ec2 create-route --route-table-id $RTB_PRIVATE_ID --destination-cidr-block 0.0.0.0/0 --nat-gateway-id $NAT_GW_ID
aws ec2 associate-route-table --subnet-id $SUBNET1_PRIVATE_ID --route-table-id $RTB_PRIVATE_ID
aws ec2 associate-route-table --subnet-id $SUBNET2_PRIVATE_ID --route-table-id $RTB_PRIVATE_ID

# Crear Grupo de Seguridad
SG_ID=$(aws ec2 create-security-group --group-name my-eks-sg --description "EKS security group" --vpc-id $VPC_ID --query 'GroupId' --output text)

# Agregar Reglas al Grupo de Seguridad
aws ec2 authorize-security-group-ingress --group-id $SG_ID --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id $SG_ID --protocol tcp --port 80 --cidr 0.0.0.0/0

# Crear Clúster EKS
aws eks create-cluster \
  --name casapp-eks-cluster \
  --role-arn eks-cluster-role \
  --resources-vpc-config subnetIds=$SUBNET1_PUBLIC_ID,$SUBNET2_PUBLIC_ID,$SUBNET1_PRIVATE_ID,$SUBNET2_PRIVATE_ID,securityGroupIds=$SG_ID


# Crear Clúster EKS
aws eks create-cluster \
  --name casapp-eks-cluster \
  --role-arn eks-cluster-role \
  --resources-vpc-config subnetIds=$SUBNET1_ID,$SUBNET2_ID,securityGroupIds=$SG_ID