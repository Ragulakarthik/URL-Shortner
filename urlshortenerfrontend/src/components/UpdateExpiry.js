import React, { useState } from 'react';
import axios from 'axios';
import '../css/UpdateExpiry.css'; // Import your CSS file for styling

const UpdateExpiry = () => {
    const [shortUrl, setShortUrl] = useState('');
    const [daysToAdd, setDaysToAdd] = useState('');
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState('');

    const handleUpdateExpiry = async (e) => {
        e.preventDefault();
        setError(''); // Reset any previous errors
        try {
            const response = await axios.put('http://localhost:8080/updateExpiry', null, {
                params: { shortUrl, daysToAdd }
            });
            setSuccess(response.data);
        } catch (error) {
            console.error("Error updating expiry:", error);
            setError('Error updating expiry: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div className="update-expiry-container">
            <h2>Update URL Expiry</h2>
            <form onSubmit={handleUpdateExpiry} className="expiry-form">
                <input 
                    type="text" 
                    value={shortUrl} 
                    onChange={(e) => setShortUrl(e.target.value)} 
                    placeholder="Enter Short URL" 
                    required 
                    className="expiry-input"
                />
                <input 
                    type="number" 
                    value={daysToAdd} 
                    onChange={(e) => setDaysToAdd(e.target.value)} 
                    placeholder="Days to Add" 
                    required 
                    className="expiry-input"
                />
                <button type="submit" className="submit-button">Update Expiry</button>
            </form>
            {success && <p className="success-message">Expiry updated successfully!</p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default UpdateExpiry;
