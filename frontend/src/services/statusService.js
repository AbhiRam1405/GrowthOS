import api from './api';

export const getTasksWithStatus = (date) =>
    api.get('/status', { params: { date } }).then(r => r.data);

export const markStatus = (taskId, date, completed) =>
    api.post(`/status/${taskId}`, null, { params: { date, completed } }).then(r => r.data);

export const getDailySummary = (date) =>
    api.get('/summary', { params: { date } }).then(r => r.data);
