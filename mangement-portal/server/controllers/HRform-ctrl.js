const HRform = require('../models/HRform')

createHRform = (req, res) => {
    const body = req.body
    if(!body) {
        return res.status(400).json({
            sucesss: false,
            error: 'You must provide a valid form'
        })
    }
    

}