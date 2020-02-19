const express = require('express')

const FormCtrl = require('../controllers/HRform-ctrl')

const router = express.Router()

router.post('/form', FormCtrl.createForm)
router.put('/form/:id', FormCtrl.updateForm)
router.put('/form/:id', FormCtrl.deleteForm)
router.put('/form/:id', FormCtrl.getFormById)
router.put('/form/:id', FormCtrl.getForm)

module.exports = router