const Device = require('../models/Device')
const mongoose = require('mongoose')

updateDevice = async (req, res) => {
    const body = req.body
    if(!body){
        return res.status(400).json({
            sucesss: false,
            error: 'No body provided for update'
        })
    }

    console.log(req.params.id)
    await Device.findById({_id: mongoose.Types.ObjectId(req.params.id)}, (err, device) => {
        if(err){
            return res.status(404).json({
                err,
                message: 'Device not found with given id'

            })
        }
        console.log(body)

        device.name = body.name
        device.location = body.location
        device.device_id = body.device_id
        device
            .save()
            .then(()=>{
                return res.status(200).json({
                    sucesss: true,
                    id: device._id,
                    message: 'Device updated!',
                })
            })
    }).catch(error => {
        return res.status(400).json({
            error,
            message: 'Unable to update device'
        })
    })
}

getDevice = async (req, res) => {
    await Device.find({}, (err, device) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!device.length) {
            return res
                .status(404)
                .json({ success: false, error: `device not found` })
        }
        return res.status(200).json({ success: true, data: device })
    }).catch(err => console.log(err))
}

createDevice = async(req, res) => {
    const body = req.body
    if(!body) {
        return res.status(400).json({
            sucesss: false,
            error: 'You must provide a valid device'
        })
    }
    const device = new Device(body)
    if(!device){
        return res.status(400).json({
            sucesss: false,
            error: 'Could not create device in given syle'
        })
    }
    device
        .save()
        .then(()=> {
            return res.status(200).json({ success: true, data: form })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'Unable to create device'
            })
        })
}

module.exports = {
    updateDevice,
    getDevice,
    createDevice,
}