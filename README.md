create a table called app_cc in mysql 
use food_insert script from resources to import foods 
insert 3 meals (Breakfast, Lunch, Dinner) into meal table

Am facut imaginile de docker de spring boot si react, pe cea de mysql o luam din docker hub.
Apoi deployment kubectl apply -f *yaml din k8 deployment
Aplicatia accesibila pe localhost:3000 - se face ceva port forwarding 
BE disponibil pe localhost:1337 (trebuie parametrizat cumva)
Ar trebui creata o imagine custom de mysql in care sa fi executat scripturile care adauga foods & meals
altfel putem insera manual atasandu-ne la pod si porning mysql de acolo,
kubectl exec -it {my_sql_pod} bash && mysql -u root -p & enter root & CLI pentru mysql

Next steps: 
1. Sa vedem cum punem un url in react app 
2. Scripturile sql de insert 
3. Microserviciul de auth (facem o alta aplicatie spring-boot) care sa aiba endpointul de /login si sa returneze tokenul
4. Trebuie modificat FE, ca auth-ul sa se faca la un url, restul de call-uri la alt url 
5. Kubernetes dintr-un control plane si 2 workeri???????
6. Utilitar de BD (Adminer compatibil cu mysql)
7. Utilitar grafic de gestiune a clusterului 
8. Utilizare terraform 
