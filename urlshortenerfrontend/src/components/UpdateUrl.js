import React, { useState } from 'react';
import axios from 'axios';
import '../css/UpdateUrl.css'; // Import your CSS file for styling

const UpdateUrl = () => {
    const [shortUrl, setShortUrl] = useState('');
    const [newOriginalUrl, setNewOriginalUrl] = useState('');
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState('');

    const handleUpdate = async (e) => {
        e.preventDefault();
        setError(''); // Reset any previous errors
        try {
            const response = await axios.put('http://localhost:8080/update', null, {
                params: { shortUrl, newOriginalUrl }
            });
            setSuccess(response.data);
        } catch (error) {
            console.error("Error updating URL:", error);
            setError('Error updating URL: ' + (error.response?.data || error.message));
        }
    };

    return (
        <div className="update-url-container">
            <h2>Update Short URL</h2>
            <form onSubmit={handleUpdate} className="url-form">
                <input 
                    type="text" 
                    value={shortUrl} 
                    onChange={(e) => setShortUrl(e.target.value)} 
                    placeholder="Enter Short URL" 
                    required 
                    className="url-input"
                />
                <input 
                    type="text" 
                    value={newOriginalUrl} 
                    onChange={(e) => setNewOriginalUrl(e.target.value)} 
                    placeholder="Enter New Original URL" 
                    required 
                    className="url-input"
                />
                <button type="submit" className="submit-button">Update</button>
            </form>
            {success && <p className="success-message">URL updated successfully!</p>}
            {error && <p className="error-message">{error}</p>}
        </div>
    );
};

export default UpdateUrl;
