import React, { useState, useEffect, useCallback } from 'react';
import { getTasksWithStatus, markStatus, getDailySummary } from '../services/statusService';
import { getWeeklyAnalytics, getRandomQuote, getSuggestion } from '../services/analyticsService';
import TaskCard from '../components/TaskCard';
import SummaryCard from '../components/SummaryCard';
import QuoteCard from '../components/QuoteCard';
import LoadingSpinner from '../components/LoadingSpinner';
import ErrorMessage from '../components/ErrorMessage';
import EmptyState from '../components/EmptyState';
import LineChart from '../charts/LineChart';
import { useNavigate } from 'react-router-dom';
import './Dashboard.css';

const today = new Date().toISOString().split('T')[0];

const Dashboard = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]);
    const [summary, setSummary] = useState(null);
    const [analytics, setAnalytics] = useState(null);
    const [quote, setQuote] = useState(null);
    const [suggestion, setSuggestion] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const loadData = useCallback(async () => {
        setLoading(true);
        setError('');
        try {
            const [tasksData, summaryData, analyticsData, quoteData, suggData] = await Promise.all([
                getTasksWithStatus(today),
                getDailySummary(today),
                getWeeklyAnalytics(),
                getRandomQuote(),
                getSuggestion(),
            ]);
            setTasks(tasksData);
            setSummary(summaryData);
            setAnalytics(analyticsData);
            setQuote(quoteData);
            setSuggestion(suggData.suggestion || '');
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    }, []);

    useEffect(() => { loadData(); }, [loadData]);

    const handleToggle = async (taskId, completed) => {
        try {
            await markStatus(taskId, today, completed);
            // Refresh tasks and summary after toggle
            const [updatedTasks, updatedSummary, updatedAnalytics] = await Promise.all([
                getTasksWithStatus(today),
                getDailySummary(today),
                getWeeklyAnalytics(),
            ]);
            setTasks(updatedTasks);
            setSummary(updatedSummary);
            setAnalytics(updatedAnalytics);
        } catch (err) {
            setError(err.message);
        }
    };

    if (loading) return <LoadingSpinner message="Loading your progress..." />;
    if (error) return <ErrorMessage message={error} onRetry={loadData} />;

    const completionPct = summary?.completionPercentage?.toFixed(1) ?? '0.0';
    const streak = summary?.streak ?? 0;

    return (
        <div className="dashboard-page">
            <div className="page-header">
                <div>
                    <h1 className="page-title">Good {getGreeting()}! 👋</h1>
                    <p className="page-sub">
                        {new Date().toLocaleDateString('en-IN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}
                    </p>
                </div>
            </div>

            {/* Quote */}
            <QuoteCard quote={quote} />

            {/* Suggestion Banner */}
            {suggestion && (
                <div className="suggestion-banner">
                    <span className="suggestion-icon">🤖</span>
                    <p className="suggestion-text">{suggestion}</p>
                </div>
            )}

            {/* Stats Grid */}
            <div className="stats-grid">
                <SummaryCard icon="✅" label="Today's Progress" value={`${completionPct}%`}
                    sub={`${tasks.filter(t => t.completed).length} / ${tasks.length} tasks`}
                    color="#4361ee" />
                <SummaryCard icon="🔥" label="Current Streak" value={`${streak} day${streak !== 1 ? 's' : ''}`}
                    sub={streak >= 1 ? 'Keep it going!' : 'Start your streak today!'} color="#f59e0b" />
                <SummaryCard icon="📊" label="Weekly Average" value={`${analytics?.weeklyAverage?.toFixed(1) ?? 0}%`}
                    sub="Last 7 days" color="#10b981" />
                <SummaryCard icon="⚠️" label="Needs Attention" value={analytics?.weakestTask ?? 'N/A'}
                    sub="Weakest task this week" color="#ef4444" />
            </div>

            {/* Tasks Section */}
            <div className="section-card">
                <div className="section-header">
                    <h2 className="section-title">📋 Today's Tasks</h2>
                    <span className="section-badge">{tasks.filter(t => t.completed).length}/{tasks.length}</span>
                </div>

                {tasks.length === 0 ? (
                    <EmptyState
                        icon="📝"
                        title="No tasks yet"
                        message="Add your first task to start tracking your daily progress!"
                        action={{ label: 'Add Your First Task', onClick: () => navigate('/tasks') }}
                    />
                ) : (
                    <div className="tasks-list">
                        {tasks.map(task => (
                            <TaskCard key={task.taskId} task={task} onToggle={handleToggle} />
                        ))}
                    </div>
                )}
            </div>

            {/* Progress Chart */}
            {analytics?.dailyProgress?.length > 0 && (
                <div className="section-card">
                    <h2 className="section-title">📈 7-Day Progress</h2>
                    <LineChart data={analytics.dailyProgress} />
                </div>
            )}
        </div>
    );
};

function getGreeting() {
    const hour = new Date().getHours();
    if (hour < 12) return 'Morning';
    if (hour < 17) return 'Afternoon';
    return 'Evening';
}

export default Dashboard;
