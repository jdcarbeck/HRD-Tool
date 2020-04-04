const mongoose = require('mongoose')

const connection_str = "mongodb+srv://admin:mongodb+srv://admin:Fof3Kc0MweKEUxDf@cluster0-xzmhp.mongodb.net/test"

mongoose
  .connect('mongodb+srv://admin:Fof3Kc0MweKEUxDf@cluster0-xzmhp.mongodb.net/Trocaire', { useNewUrlParser: true, useUnifiedTopology: true})
  .catch(e => {
    console.error('Connection error', e.message)
  })

const db = mongoose.connection

module.exports = db

