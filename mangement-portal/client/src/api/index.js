import axios from 'axios'
const FileDownload = require('js-file-download');

const api = axios.create({
    baseURL: 'http://localhost:3000/api',
})

// here is where the methods that are linked to the server router are defined
export const getAllForms = () => api.get(`/form`)
export const getFormById = id => api.get(`/form/${id}`)
export const updateFormRead = id => api.put(`/form/${id}`)
export const download = () => {
    api.get(`/download`).then((response) => {
        FileDownload(response.data, 'forms.csv');
   });
}

const apis = {
    getAllForms,
    getFormById,
    updateFormRead,
    download,
}

export default apis