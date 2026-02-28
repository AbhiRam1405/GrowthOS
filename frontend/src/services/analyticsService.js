import api from './api';

export const getWeeklyAnalytics = () =>
    api.get('/analytics/weekly').then(r => r.data);

export const getCategoryAnalytics = () =>
    api.get('/analytics/category').then(r => r.data);

export const getSuggestion = () =>
    api.get('/analytics/suggestions').then(r => r.data);

export const getRandomQuote = () =>
    api.get('/quotes/random').then(r => r.data);
