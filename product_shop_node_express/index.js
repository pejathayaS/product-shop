import express from 'express';
import bodyParser from 'body-parser';

import productsRoutes from './routes/products.js'

const url = 'mongodb://127.0.0.1:27017/productDB';

const app = express();

const PORT = 8084;

app.use(express.json());

app.use('/products', productsRoutes);

app.get('/',(req,resp)=> { 
  resp.send("Product HomePage");
});

app.listen(PORT,()=> console.log(`Server Running on Port :: http://localhost:${PORT}`));

