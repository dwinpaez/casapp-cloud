#!/bin/bash

set -e

# Configuraciones iniciales
REGION="us-east-1"
ROLE_NAME="eks-cluster-role"
POLICY_NAME="PassRolePolicy"
USER_NAME="dwinpaez"

# Nombre del clúster EKS
CLUSTER_NAME="casapp-eks-cluster"

# Eliminar el clúster EKS
echo "Eliminando el clúster EKS..."
aws eks delete-cluster --name $CLUSTER_NAME
echo "Esperando a que el clúster EKS sea eliminado..."
aws eks wait cluster-deleted --name $CLUSTER_NAME

# Obtener el ARN del rol IAM
ROLE_ARN=$(aws iam get-role --role-name $ROLE_NAME --query 'Role.Arn' --output text 2>/dev/null || echo "None")
if [ "$ROLE_ARN" != "None" ]; then
    # Desasociar la política del rol IAM
    POLICY_ARN="arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
    echo "Desasociando la política AmazonEKSClusterPolicy del rol IAM..."
    aws iam detach-role-policy --role-name $ROLE_NAME --policy-arn $POLICY_ARN

    # Eliminar el rol IAM
    echo "Eliminando el rol IAM..."
    aws iam delete-role --role-name $ROLE_NAME
fi

# Desasociar la política del usuario
echo "Desasociando la política del usuario..."
POLICY_ARN=$(aws iam list-policies --query "Policies[?PolicyName=='$POLICY_NAME'].Arn" --output text)
if [ -n "$POLICY_ARN" ]; then
    aws iam detach-user-policy --user-name $USER_NAME --policy-arn $POLICY_ARN
    # Eliminar la política
    echo "Eliminando la política..."
    aws iam delete-policy --policy-arn $POLICY_ARN
fi

# Obtener las subredes
echo "Obteniendo subredes..."
SUBNET_IDS=$(aws ec2 describe-subnets --query "Subnets[?VpcId=='$VPC_ID'].SubnetId" --output text)
for SUBNET_ID in $SUBNET_IDS; do
    echo "Eliminando subred $SUBNET_ID..."
    aws ec2 delete-subnet --subnet-id $SUBNET_ID
done

# Obtener y eliminar tablas de rutas
echo "Obteniendo tablas de rutas..."
ROUTE_TABLE_IDS=$(aws ec2 describe-route-tables --filters "Name=vpc-id,Values=$VPC_ID" --query "RouteTables[*].RouteTableId" --output text)
for RTB_ID in $ROUTE_TABLE_IDS; do
    echo "Eliminando tabla de rutas $RTB_ID..."
    aws ec2 delete-route-table --route-table-id $RTB_ID
done

# Obtener y eliminar el Internet Gateway
echo "Obteniendo Internet Gateway..."
IGW_ID=$(aws ec2 describe-internet-gateways --filters "Name=attachment.vpc-id,Values=$VPC_ID" --query "InternetGateways[*].InternetGatewayId" --output text)
if [ -n "$IGW_ID" ]; then
    echo "Desasociando Internet Gateway $IGW_ID..."
    aws ec2 detach-internet-gateway --internet-gateway-id $IGW_ID --vpc-id $VPC_ID
    echo "Eliminando Internet Gateway $IGW_ID..."
    aws ec2 delete-internet-gateway --internet-gateway-id $IGW_ID
fi

# Obtener y eliminar la NAT Gateway
echo "Obteniendo NAT Gateway..."
NAT_GW_ID=$(aws ec2 describe-nat-gateways --filter "Name=vpc-id,Values=$VPC_ID" --query "NatGateways[*].NatGatewayId" --output text)
if [ -n "$NAT_GW_ID" ]; then
    echo "Eliminando NAT Gateway $NAT_GW_ID..."
    aws ec2 delete-nat-gateway --nat-gateway-id $NAT_GW_ID
    echo "Esperando a que la NAT Gateway sea eliminada..."
    aws ec2 wait nat-gateway-deleted --nat-gateway-id $NAT_GW_ID
fi

# Eliminar la VPC
echo "Eliminando la VPC..."
aws ec2 delete-vpc --vpc-id $VPC_ID

echo "Todos los recursos han sido eliminados."
