import React, { useState } from 'react';
import './TaskCompletionModal.css';

const TaskCompletionModal = ({ task, onSave, onCancel, loading }) => {
    const [note, setNote] = useState('');
    const [timeSpent, setTimeSpent] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!note.trim()) {
            setError('Completion note is required.');
            return;
        }
        onSave({
            note: note.trim(),
            timeSpent: timeSpent ? parseInt(timeSpent) : null
        });
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content completion-modal">
                <div className="modal-header">
                    <h2>🎯 Mark as Complete</h2>
                    <button className="close-btn" onClick={onCancel}>&times;</button>
                </div>
                <div className="modal-body">
                    <p className="task-reference">Task: <strong>{task.title}</strong></p>
                    <form id="completion-form" onSubmit={handleSubmit}>
                        <div className="form-group">
                            <label className="form-label">Completion Note</label>
                            <textarea
                                className={`form-input area-input ${error ? 'input-error' : ''}`}
                                placeholder="What work was done?"
                                value={note}
                                onChange={(e) => { setNote(e.target.value); setError(''); }}
                                maxLength={2000}
                                rows={4}
                            />
                            {error && <span className="form-error">{error}</span>}
                        </div>
                        <div className="form-group">
                            <label className="form-label">Time Spent (minutes, optional)</label>
                            <input
                                type="number"
                                className="form-input"
                                placeholder="e.g. 30"
                                value={timeSpent}
                                onChange={(e) => setTimeSpent(e.target.value)}
                                min={0}
                            />
                        </div>
                    </form>
                </div>
                <div className="modal-footer">
                    <button type="button" className="btn-secondary" onClick={onCancel}>Cancel</button>
                    <button type="submit" form="completion-form" className="btn-primary" disabled={loading}>
                        {loading ? 'Saving...' : 'Save Completion'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default TaskCompletionModal;
