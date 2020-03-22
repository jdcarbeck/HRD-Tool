const mongoose = require('mongoose')
const Schema = mongoose.Schema

const Device = new Schema(
    {
        device_id: { type: String, required: true },
        name: { type: [String], required: true },
        location: { type: [String], required: true }
    },
    {timestamps: true},
)

module.exports = mongoose.model('Device', Device)