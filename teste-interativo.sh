#!/bin/bash

# Definir variáveis de URL base
USER_SERVICE_URL="http://localhost:8081/users"
TRACKING_SERVICE_URL="http://localhost:8082/packages"

# Função para mostrar o menu
show_menu() {
    echo "Selecione uma opção:"
    echo "1) Criar um novo usuário"
    echo "2) Adicionar um pacote a um usuário"
    echo "3) Criar um pacote no TrackingService"
    echo "4) Atualizar o status de um pacote no TrackingService"
    echo "5) Recuperar status de um pacote via UserService (Feign)"
    echo "6) Exibir detalhes do usuário"
    echo "7) Sair"
}

# Função para criar um novo usuário
create_user() {
    read -p "Digite o email do novo usuário: " email
    curl -X POST "$USER_SERVICE_URL" -H "Content-Type: application/json" -d "{\"email\":\"$email\"}"
    echo ""
}

# Função para adicionar um pacote a um usuário
add_package_to_user() {
    read -p "Digite o ID do usuário: " user_id
    read -p "Digite o número de rastreamento do pacote: " tracking_number
    curl -X POST "$USER_SERVICE_URL/$user_id/packages" -H "Content-Type: application/json" -d "{\"trackingNumber\":\"$tracking_number\"}"
    echo ""
}

# Função para criar um pacote no TrackingService
create_package_tracking_service() {
    read -p "Digite o número de rastreamento do pacote: " tracking_number
    read -p "Digite o status inicial do pacote: " status
    curl -X POST "$TRACKING_SERVICE_URL" -H "Content-Type: application/json" -d "{\"trackingNumber\":\"$tracking_number\", \"status\":\"$status\"}"
    echo ""
}

# Função para atualizar o status de um pacote no TrackingService
update_package_status() {
    read -p "Digite o número de rastreamento do pacote: " tracking_number
    read -p "Digite o novo status: " status
    curl -X PUT "$TRACKING_SERVICE_URL/$tracking_number" -H "Content-Type: application/json" -d "\"$status\""
    echo ""
}

# Função para recuperar o status de um pacote via UserService (Feign)
get_package_status_via_userservice() {
    read -p "Digite o ID do usuário: " user_id
    curl -X GET "$USER_SERVICE_URL/$user_id"
    echo ""
}

# Função para exibir detalhes do usuário
show_user_details() {
    read -p "Digite o ID do usuário: " user_id
    curl -X GET "$USER_SERVICE_URL/$user_id"
    echo ""
}

# Função principal
while true; do
    show_menu
    read -p "Escolha uma opção: " choice
    case $choice in
        1) create_user ;;
        2) add_package_to_user ;;
        3) create_package_tracking_service ;;
        4) update_package_status ;;
        5) get_package_status_via_userservice ;;
        6) show_user_details ;;
        7) echo "Saindo..."; exit 0 ;;
        *) echo "Opção inválida!";;
    esac
    echo ""
done
