import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:3000/api',
})

// here is where the methods that are linked to the server router are defined
export const getAllForms = () => api.get(`/form`)
export const getFormById = id => api.get(`/form/${id}`)

const apis = {
    getAllForms,
    getFormById,
}

export default apis