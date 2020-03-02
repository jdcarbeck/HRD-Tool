const Form = require('../models/HRform')

createForm = (req, res) => {
    const body = req.body
    if(!body) {
        return res.status(400).json({
            sucesss: false,
            error: 'You must provide a valid form'
        })
    }

    const form = new Form(body)

    if(!form){
        return res.status(400).json({
            sucesss: false,
            error: 'Could not create form in given syle'
        })
    }

    form
        .save()
        .then(()=> {
            return res.status(201).json({
                sucesss: true,
                id: form._id,
                message: 'Form created!'
            })
        })
        .catch(error => {
            return res.status(400).json({
                error,
                message: 'Unable to create form'
            })
        })
}

updateForm = async (req, res) => {
    const body = req.body

    if(!body) {
        return res.status(400).json({
            sucesss: false,
            error: 'No body provided for update'
        })
    }

    Form.findOne({ _id: req.params.id }, (err, form) => {
        if(err){
            return res.status(404).json({
                err,
                message: 'Form not found with given id'

            })
        }
        // The values are added here
        // form.value = body.value
        form
            .save()
            .then(()=> {
                return res.status(200).json({
                    sucesss: true,
                    id: form._id,
                    message: 'Form updated!',
                })
            })
            .catch(error => {
                return res.status(404).json({
                    error,
                    message: 'Form not updated!',
                })
            })
    })
}

deleteForm = async (req, res) => {
    await Form.findOneAndDelete({ _id: req.params.id }, (err, form) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!form) {
            return res
                .status(404)
                .json({ success: false, error: `Form not found` })
        }

        return res.status(200).json({ success: true, data: form })
    }).catch(err => console.log(err))
}

getFormById = async(req, res) => {
    await Form.findOne({ _id: req.params.id }, (err, form) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }

        if (!form) {
            return res
                .status(404)
                .json({ success: false, error: `Form not found` })
        }
        return res.status(200).json({ success: true, data: form })
    }).catch(err => console.log(err))
}

getForm = async (req, res) => {
    await Form.find({}, (err, form) => {
        if (err) {
            return res.status(400).json({ success: false, error: err })
        }
        if (!form.length) {
            return res
                .status(404)
                .json({ success: false, error: `Form not found` })
        }
        return res.status(200).json({ success: true, data: form })
    }).catch(err => console.log(err))
}

module.exports = {
    createForm,
    updateForm,
    deleteForm,
    getForm,
    getFormById,
}