import express from 'express';
const router = express.Router();
import mongodb from 'mongodb';
const MongoClient = mongodb.MongoClient;

const url = 'mongodb://127.0.0.1:27017/productDB';
const dbName = 'productDB';

//connection test
MongoClient.connect(url, {
    useNewUrlParser: true,
    useUnifiedTopology: true
}, (err, client) => {
    if (err) {
        return console.log(err);
    }
    const db = client.db('productDB');
    db.collection('Product',function(err,collection){
        if(err) console.log(err);
    });
    console.log(`MongoDB Connection successfull: ${url}`);
});

//product get all operation
router.get('/', (req, resp) => {
    MongoClient.connect(url, (err,client) => {
        if(err) return console.log(err);

        const db = client.db(dbName);
        db.collection('Product',(err,collection)=>{
            if(err) console.log(err);
            collection.find().toArray(function(err, items) {
                if(err) throw err;    
                resp.send(items);   
            });
        });
    });
}
);

//product add operation
router.post('/', (req, resp) => {
    const product = req.body;
    MongoClient.connect(url,(error,client)=>{
        client.db(dbName).collection('Product',(err,collection)=> {
            if(err) console.log(err);
            collection.insertOne(product).then(res => resp.send(`Product with the name ${product._id} added to the database`));
        })
    })
});

//product get by :id operation
router.get('/:id', (req, resp) => {
    const { id } = req.params;

    MongoClient.connect(url, (err,client) => {
        if(err) return console.log(err);

        const db = client.db(dbName);
        db.collection('Product',(err,collection)=>{
            if(err) console.log(err);
           collection.findOne({'_id':id}).then((p)=> resp.send(p));
        });
    });
});

//product delete by :id operation
router.delete('/:id', (req, resp) => {
    const { id } = req.params;

    MongoClient.connect(url,(error,client)=>{
        client.db(dbName).collection('Product',(err,collection)=>{
            collection.deleteOne({'_id':id}).then(p=> resp.send(`Product with the id ${id} is deleted from the database`));
        })
    });
});

//product replace by :id operation
router.patch('/:id', (req, resp) => {
    const product = req.body;
    const { id } = req.params;
    MongoClient.connect(url,(error,client)=>{
        client.db(dbName).collection('Product',(err,collection)=>{
            collection.replaceOne({'_id':id},product,(err,result)=>{
                if(err) console.log(err);
                resp.send(`Product with the Id ${product._id} replaced to the database`)
            })
        })
    });
});

export default router;
