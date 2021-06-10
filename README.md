# product-shop
A brief look into the deisgn and implementation of Product-shop

product-shop-ui - An angular web application which provides GUI to perform CRUD operations on the product. 

product-service-ms - A Spring boot application with mongoDB as a backend exposing the REST endpoints for product operations.Also establishes connection to kafka and pushes the incoming data to kafka topic.

downstream-service-ms : A spring boot microservice which listens to specific topics in kafka and downstreams data to third party applications.

More detail about these services are captured in the "Case-Study-Design and Implementation" Document 


product_shop_node_express :- An equivalent of Product-service server written in Javascript. Its a node.js server program with established connection to MongoDB performing CRUD operations on the product. This exposes REST endpoints for the angular-ui to establish connection and push/pull the data. 
