const mongoose = require('mongoose')
const Schema = mongoose.Schema

const Form = new Schema(
    {
        id: { type: String, required: true },
        time: { type: [String], required: true },
        type: { type: [String], required: true }
    },
    { timestamps: true },
)

module.exports = mongoose.model('forms', Form)