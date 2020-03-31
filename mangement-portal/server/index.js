const express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')

const db = require('./db')
const formRouter = require('./routes/HRform-router')
const rsa = require('./crypto/rsa')
const NodeRSA = require('node-rsa');

const app = express()
const apiPort = 3000
// const key = new NodeRSA();
// key.generateKeyPair(3072, 24839);

// const derPublicKey = key.exportKey('pkcs8-public-der')

rsa.initLoadServerKeys(__dirname)

app.use(bodyParser.urlencoded({ extended:true }))
app.use(cors())
app.use(bodyParser.json())

db.on('error', console.error.bind(console, 'MongoDB connection error:'))

app.post('/key', (_,res) =>{
  var derB64PublicKey  = Buffer.from(rsa.serverPub , 'binary').toString('base64');
  res.send(derB64PublicKey)
})

app.use('/api', formRouter)

app.listen(apiPort, () => console.log(`Server running on port ${apiPort}`))
