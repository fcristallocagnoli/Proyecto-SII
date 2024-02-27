
echo Iniciando microservicio de gestion de correctores ...

:: TODO: Mejorar este script
:: De momento solo consigo que funcione si se ejecuta todo en el mismo CALL. Ademas, siempre ejecuta los tests
CALL mvn clean test spring-boot:start -f ./GestionCorrectores/pom.xml && echo Pulse una tecla para finalizar ambos microservicios && PAUSE && echo Deteniendo microservicio gestion de correctores ... && mvn spring-boot:stop -f ./GestionCorrectores/pom.xml