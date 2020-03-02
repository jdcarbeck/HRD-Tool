const mongoose = require('mongoose')


mongoose
  .connect('mongodb+srv://admin:Fof3Kc0MweKEUxDf@cluster0-xzmhp.mongodb.net/HRData', { useNewUrlParser: true })
  .catch(e => {
    console.error('Connection error', e.message)
  })

const db = mongoose.connection

module.exports = db
