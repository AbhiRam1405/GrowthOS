import api from './api';

export const getAllTasks = () => api.get('/tasks').then(r => r.data);

export const createTask = (task) => api.post('/tasks', task).then(r => r.data);

export const updateTask = (id, task) => api.put(`/tasks/${id}`, task).then(r => r.data);

export const deleteTask = (id) => api.delete(`/tasks/${id}`);
