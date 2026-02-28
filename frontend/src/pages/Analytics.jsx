import React, { useState, useEffect, useCallback } from 'react';
import { getWeeklyAnalytics, getCategoryAnalytics, getSuggestion } from '../services/analyticsService';
import SummaryCard from '../components/SummaryCard';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import LineChart from '../charts/LineChart';
import BarChart from '../charts/BarChart';
import './Analytics.css';

const Analytics = () => {
    const [analytics, setAnalytics] = useState(null);
    const [categoryData, setCategoryData] = useState({});
    const [suggestion, setSuggestion] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const loadData = useCallback(async () => {
        setLoading(true);
        setError('');
        try {
            const [analyticsData, catData, suggData] = await Promise.all([
                getWeeklyAnalytics(),
                getCategoryAnalytics(),
                getSuggestion(),
            ]);
            setAnalytics(analyticsData);
            setCategoryData(catData);
            setSuggestion(suggData.suggestion || '');
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => { loadData(); }, [loadData]);

    if (loading) return <LoadingSpinner message="Crunching your analytics..." />;
    if (error) return <ErrorMessage message={error} onRetry={loadData} />;

    const hasData = analytics?.dailyProgress?.some(d => d.completionPercentage > 0);

    return (
        <div className="analytics-page">
            <div className="page-header">
                <h1 className="page-title">📊 Analytics</h1>
                <p className="page-sub">Performance insights for the last 7 days</p>
            </div>

            {/* KPI Cards */}
            <div className="analytics-stats-grid">
                <SummaryCard icon="📈" label="Weekly Average" value={`${analytics?.weeklyAverage?.toFixed(1) ?? 0}%`}
                    sub="Last 7 days completion" color="#4361ee" />
                <SummaryCard icon="🔥" label="Current Streak" value={`${analytics?.currentStreak ?? 0} days`}
                    sub="Active daily streak" color="#f59e0b" />
                <SummaryCard icon="🏆" label="Longest Streak" value={`${analytics?.longestStreak ?? 0} days`}
                    sub="All-time best" color="#10b981" />
                <SummaryCard icon="💪" label="Strongest Task" value={analytics?.strongestTask ?? 'N/A'}
                    sub="Most completed this week" color="#8b5cf6" />
                <SummaryCard icon="⚠️" label="Weakest Task" value={analytics?.weakestTask ?? 'N/A'}
                    sub="Needs more focus" color="#ef4444" />
            </div>

            {/* AI Suggestion */}
            {suggestion && (
                <div className="analytics-suggestion">
                    <div className="sugg-header">
                        <span>🤖</span>
                        <span>AI Insight</span>
                    </div>
                    <p className="sugg-body">{suggestion}</p>
                </div>
            )}

            {!hasData ? (
                <EmptyState
                    icon="📉"
                    title="Not enough data yet"
                    message="Complete some tasks over several days and your analytics will appear here automatically!"
                />
            ) : (
                <>
                    {/* Line Chart */}
                    <div className="analytics-card">
                        <h2 className="analytics-card-title">📅 7-Day Completion Trend</h2>
                        <LineChart data={analytics?.dailyProgress ?? []} />
                    </div>

                    {/* Bar Chart */}
                    {Object.keys(categoryData).length > 0 && (
                        <div className="analytics-card">
                            <h2 className="analytics-card-title">🗂️ Completions by Category</h2>
                            <BarChart data={categoryData} />
                        </div>
                    )}
                </>
            )}
        </div>
    );
};

export default Analytics;
