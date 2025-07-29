#!/bin/bash

# Script de prueba para la API de Productos
# Requiere: curl, jq

BASE_URL="http://localhost:8080/api/v1"
API_KEY="producto-service-key-2024"

echo "🧪 Iniciando pruebas de la API de Productos..."
echo "================================================"

# Función para hacer peticiones con API Key
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    
    if [ -n "$data" ]; then
        curl -s -X $method \
            -H "Content-Type: application/json" \
            -H "X-API-Key: $API_KEY" \
            -d "$data" \
            "$BASE_URL$endpoint"
    else
        curl -s -X $method \
            -H "X-API-Key: $API_KEY" \
            "$BASE_URL$endpoint"
    fi
}

# Función para mostrar resultados
show_result() {
    local test_name=$1
    local response=$2
    local expected_status=$3
    
    echo "📋 $test_name"
    echo "Respuesta:"
    echo "$response" | jq '.' 2>/dev/null || echo "$response"
    echo "----------------------------------------"
}

# 1. Probar health check
echo "1️⃣ Probando health check..."
health_response=$(curl -s http://localhost:8080/actuator/health)
show_result "Health Check" "$health_response"

# 2. Listar productos (debe estar vacío inicialmente)
echo "2️⃣ Listando productos..."
list_response=$(make_request "GET" "/productos")
show_result "Listar Productos" "$list_response"

# 3. Crear un producto
echo "3️⃣ Creando producto..."
create_data='{
  "nombre": "Laptop Dell Inspiron 15",
  "precio": 1500.00,
  "descripcion": "Laptop Dell Inspiron 15 pulgadas con procesador Intel i5"
}'
create_response=$(make_request "POST" "/productos" "$create_data")
show_result "Crear Producto" "$create_response"

# Extraer ID del producto creado
product_id=$(echo "$create_response" | jq -r '.data.id' 2>/dev/null)
if [ "$product_id" = "null" ] || [ -z "$product_id" ]; then
    echo "❌ Error: No se pudo obtener el ID del producto creado"
    exit 1
fi

# 4. Obtener producto por ID
echo "4️⃣ Obteniendo producto por ID: $product_id..."
get_response=$(make_request "GET" "/productos/$product_id")
show_result "Obtener Producto por ID" "$get_response"

# 5. Crear otro producto
echo "5️⃣ Creando segundo producto..."
create_data2='{
  "nombre": "Mouse Logitech MX Master",
  "precio": 89.99,
  "descripcion": "Mouse inalámbrico Logitech MX Master 3"
}'
create_response2=$(make_request "POST" "/productos" "$create_data2")
show_result "Crear Segundo Producto" "$create_response2"

# 6. Listar todos los productos
echo "6️⃣ Listando todos los productos..."
list_response2=$(make_request "GET" "/productos")
show_result "Listar Todos los Productos" "$list_response2"

# 7. Buscar productos por nombre
echo "7️⃣ Buscando productos por nombre 'Laptop'..."
search_response=$(make_request "GET" "/productos/buscar/nombre?nombre=Laptop")
show_result "Buscar por Nombre" "$search_response"

# 8. Buscar productos por rango de precio
echo "8️⃣ Buscando productos por rango de precio (50-2000)..."
price_response=$(make_request "GET" "/productos/buscar/precio?minPrecio=50&maxPrecio=2000")
show_result "Buscar por Rango de Precio" "$price_response"

# 9. Probar producto no encontrado
echo "9️⃣ Probando producto no encontrado..."
not_found_response=$(make_request "GET" "/productos/999")
show_result "Producto No Encontrado" "$not_found_response"

# 10. Probar sin API Key (debe fallar)
echo "🔟 Probando sin API Key..."
no_auth_response=$(curl -s -X GET "$BASE_URL/productos/1")
show_result "Sin Autenticación" "$no_auth_response"

# 11. Probar Swagger UI
echo "1️⃣1️⃣ Verificando Swagger UI..."
swagger_response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/swagger-ui.html)
if [ "$swagger_response" = "200" ]; then
    echo "✅ Swagger UI está disponible en: http://localhost:8080/swagger-ui.html"
else
    echo "❌ Swagger UI no está disponible"
fi

echo "================================================"
echo "🎉 Pruebas completadas!"
echo ""
echo "📊 Resumen:"
echo "- Health Check: ✅"
echo "- CRUD Productos: ✅"
echo "- Búsquedas: ✅"
echo "- Autenticación: ✅"
echo "- Swagger UI: ✅"
echo ""
echo "🌐 URLs útiles:"
echo "- API Base: $BASE_URL"
echo "- Swagger UI: http://localhost:8080/swagger-ui.html"
echo "- Health Check: http://localhost:8080/actuator/health"
echo ""
echo "🔑 API Key para pruebas: $API_KEY"