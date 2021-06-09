import express from 'express';
import { v4 as uuidv4 } from 'uuid'; 
const router = express.Router();

let products = [
    {
        id: "ring",
        price: 10000,
        quantity: 1,
        name: "Fredo"
    },

    {
        id: "dow",
        price: 1000,
        quantity: 1,
        name: "sam"
    }
]

router.get('/', (req, resp) => {
    resp.send(products);
}
);

router.post('/',(req,resp)=> {
   const product = req.body;
   products.push(product);

    resp.send('Product with the name ${product.id} added tp the database');
});

router.get('/:id',(req,resp)=> {
    const {id} = req.params;

    console.log(id);
    resp.send(products.find(product=> product.id === id));
});

router.delete('/:id', (req,resp)=> {
    const {id} = req.params;
    products =  products.filter((product) => product.id !== id);

    resp.send(products);
});

router.patch('/:id', (req,resp) => {
    const {id} = req.params;

    const {price,quantity,name} = req.body;

    const productToUpdate = products.find((prod) => prod.id === id);

    console.log(productToUpdate);

    if(price) {
        productToUpdate.price = price;
    }
    if(quantity) {
        productToUpdate.quantity = quantity;
    }
    if(name) {
        productToUpdate.name = name;
    }
});

export default router;