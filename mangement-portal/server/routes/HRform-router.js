const express = require('express')

const FormCtrl = require('../controllers/HRform-ctrl')
const DeviceCtrl = require('../controllers/Device-ctrl')

const router = express.Router()

router.post('/form', FormCtrl.createForm)
router.put('/form/:id', FormCtrl.updateForm)
router.delete('/form/:id', FormCtrl.deleteForm)
router.get('/form/:id', FormCtrl.getFormById)
router.get('/form/', FormCtrl.getForm)
router.get('/download', FormCtrl.download)
router.get('/devices', DeviceCtrl.getDevice)
router.put('/devices/:id', DeviceCtrl.updateDevice)
router.post('devices', DeviceCtrl.createDevice)

module.exports = router
