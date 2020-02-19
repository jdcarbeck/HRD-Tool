const mongoose = require('mongoose')


mongoose
  .connect('mongodb+srv://admin:TrinityCollege2020@cluster0-xzmhp.mongodb.net/HRData?retryWrites=true&w=majority', { useNewUrlParser: true })
  .catch(e => {
    console.error('Connection error', e.message)
  })

const db = mongoose.connection

module.exports = db
