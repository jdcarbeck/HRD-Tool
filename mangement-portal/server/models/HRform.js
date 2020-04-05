const mongoose = require('mongoose')
const Schema = mongoose.Schema

const GENDER = ["Male", "Female"]
const KNOWN = ["Known", "Unknown"]
const IFKNOWN = ["Current partner","Former partner" ,"Relative" ,"Neighbor" ,"Friend" ,"Association" ,"Other"]

const AGE = ["0-9", "10-19", "20-29", "30-39", "40-49", "50+"]
const AREA = ["D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10", "D11", "D12", "D13", "D14", "D15", "D16", "D17", "D18", "D19", "D20"]



const Form = new Schema(
    {
        device_id: { type: String, required: true },
        incident_date: {type: Date},
        attention_date: {type: Date},
        gender: { type: String, enum: GENDER},
        age_range: { type: String, enum: AGE},
        municipality: { type: String},
        community: { type: String},
        support_sought: { type: Array},
        support_offered: { type: Array},
        support_referred: { type: Array},
        physical_abuse: { type: Boolean},
        physical_abuse_suffered: { type: Array},
        emotional_abuse: { type: Boolean},
        emotional_abuse_suffered: { type: Array},
        sexual_abuse: { type: Boolean},
        sexual_abuse_suffered: { type: Array},
        forced_marriage: { type: Boolean},
        perpetrator_gender: { type: String, enum: GENDER},
        perpetrator_known: { type: String, enum: KNOWN},
        perpetrator_association: { type: String, enum: IFKNOWN},
    },
    { timestamps: true },
)

module.exports = mongoose.model('Form', Form)