import PriorityBadge from './PriorityBadge';
import './TaskDetailsModal.css';

const TaskDetailsModal = ({ task, onClose }) => {
    if (!task) return null;

    const isCompleted = task.status === 'COMPLETED';

    const formatDate = (dateStr) => {
        if (!dateStr) return '—';
        const date = new Date(dateStr);
        const d = String(date.getDate()).padStart(2, '0');
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const y = date.getFullYear();
        const hh = String(date.getHours()).padStart(2, '0');
        const mm = String(date.getMinutes()).padStart(2, '0');
        return `${d}-${m}-${y} ${hh}:${mm}`;
    };

    return (
        <div className="modal-overlay">
            <div className="modal-content details-modal">
                <div className="modal-header">
                    <h2>🔍 Task Details</h2>
                    <button className="close-btn" onClick={onClose}>&times;</button>
                </div>
                <div className="modal-body">
                    <div className="detail-section">
                        <label className="detail-label">Title</label>
                        <div className="detail-value title-value">{task.title}</div>
                    </div>

                    <div className="detail-row">
                        <div className="detail-section">
                            <label className="detail-label">Status</label>
                            <div className="detail-value">
                                <span className={`status-pill ${task.status.toLowerCase()}`}>
                                    {task.status}
                                </span>
                            </div>
                        </div>
                        <div className="detail-section">
                            <label className="detail-label">Category</label>
                            <div className="detail-value">{task.category}</div>
                        </div>
                        <div className="detail-section">
                            <label className="detail-label">Priority</label>
                            <div className="detail-value">
                                <PriorityBadge priority={task.priority} />
                            </div>
                        </div>
                    </div>

                    <div className="detail-section">
                        <label className="detail-label">Frequency</label>
                        <div className="detail-value">{task.frequency}</div>
                    </div>

                    {isCompleted && (
                        <>
                            <hr className="detail-divider" />
                            <div className="detail-section">
                                <label className="detail-label">Completion Note</label>
                                <div className="detail-value note-value">{task.completionNote}</div>
                            </div>

                            <div className="detail-row">
                                <div className="detail-section">
                                    <label className="detail-label">Time Spent</label>
                                    <div className="detail-value">{task.timeSpent ? `${task.timeSpent} mins` : 'Not recorded'}</div>
                                </div>
                                <div className="detail-section">
                                    <label className="detail-label">Completed At</label>
                                    <div className="detail-value">{formatDate(task.completedAt)}</div>
                                </div>
                            </div>
                        </>
                    )}
                </div>
                <div className="modal-footer">
                    <button className="btn-primary" onClick={onClose}>Close</button>
                </div>
            </div>
        </div>
    );
};

export default TaskDetailsModal;
