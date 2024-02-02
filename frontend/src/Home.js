import React, { useState, useEffect } from 'react';

const Home = () => {
    const [activities, setActivities] = useState([]);
    const [description, setDescription] = useState('');
    const [maliciousBlock, setMaliciousBlock] = useState('');
    const [integrityStatus, setIntegrityStatus] = useState('');

    useEffect(() => {
        // Fetch activities and blockchain status on component mount
        fetch('http://localhost:8080/activities')
            .then(response => response.json())
            .then(data => setActivities(data))
            .catch(error => console.error('Error fetching activities:', error));
    }, []);

    const handleAddTask = () => {
        // Make a POST request to add a task
        fetch('http://localhost:8080/addActivity', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ description }),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // Clear the activity input after adding a task
                setDescription('');

                // Reload the entire page to fetch and display the updated list of activities
               window.location.reload();
            })
            .catch(error => console.error('Error adding task:', error));
    };
    const handleDoneButtonClick = (id) => {
        // Make a POST request to update the activity as done
        fetch(`http://localhost:8080/updateActivity/${id}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // Reload the entire page to fetch and display the updated list of activities
                window.location.reload();
            })
            .catch(error => console.error('Error updating activity:', error));
    };
    const handleInsertMaliciousBlock = () => {
        // Make a POST request to insert a malicious block
        fetch('http://localhost:8080/insertMaliciousBlock', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ maliciousBlock }),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                // Clear the activity input after adding a task
                fetch('http://localhost:8080/isValid')
                    .then(response => response.text())
                    .then(validity => {
                        if (validity === 'Invalid Blockchain') {
                            // Display a success message if the blockchain is valid
                            alert('Malicious block inserted! Blockchain is INVALID.');
                        }
                    })
            })
            .catch(error => console.error('Error inserting malicious block:', error));
    };
    const checkBlockchainIntegrity = () => {
        // Make a GET request to check blockchain integrity
        fetch('http://localhost:8080/isValid')
            .then(response => response.text())
            .then(validity => {
                // Set the integrity status in the state
                setIntegrityStatus(validity);
            })
            .catch(error => console.error('Error checking blockchain integrity:', error));
    };
    return (
        <div className="container">
            <div style={{marginBottom: '35px', background: "#702963", width: "1290px", height: "70px", display: 'flex', alignItems: 'center', justifyContent: 'center'}}>


            <h2 style={{color: "white"}}> My Blockchain To-Do List</h2>
            </div>
            <div className="mb-4">

                <div className="input-group"style={{width: "520px"}}>
                    <input
                        type="text"
                        placeholder="Enter activity"
                        className="form-control"
                        value={description}
                        onChange={e => setDescription(e.target.value)}
                    />
                    <div className="input-group-append">
                        <button style={{marginLeft: "25px", background: "#D8BFD8", border:'none', color: 'black'}} className="btn btn-primary" onClick={handleAddTask}>
                            Add New Activity
                        </button>
                    </div>
                </div>
            </div>

            <div className="mb-4" style={{width: "540px"}}>
                <div className="input-group">
                    <input
                        type="text"
                        placeholder="Enter Malicious Block"
                        className="form-control"
                        value={maliciousBlock}
                        onChange={e => setMaliciousBlock(e.target.value)}
                    />
                    <div className="input-group-append">
                        <button style={{marginLeft: "25px", background: '#953553', border: 'none'}}   className="btn btn-danger" onClick={handleInsertMaliciousBlock}>
                            Add Malicious Block
                        </button>
                    </div>
                </div>
            </div>

            <div>
                <table style={{ width: '65%', marginTop: '50px', marginLeft: "20%" }}>
                    <thead>
                    <tr>
                        <th style={{ padding: '10px', border: '1px solid #ccc', width: '1%' }}>ID</th>
                        <th style={{ padding: '10px', border: '1px solid #ccc', width: '20%' }}>Activity</th>
                        <th style={{ padding: '10px', border: '1px solid #ccc', width: '10%' }}>Done</th>
                        <th style={{ padding: '10px', border: '1px solid #ccc', width: '5%' }}></th>
                    </tr>
                    </thead>
                    <tbody>
                    {activities.map(activity => (
                        <tr key={activity.id} style={{ background: String(activity.done) === 'true' ? '#c8e6c9' : '#ffcdd2' }}>
                            <td style={{ padding: '10px', border: '1px solid #ccc', width: '1%' }}>
                                {activity.activityId}
                            </td>
                            <td style={{ padding: '10px', border: '1px solid #ccc', width: '20%' }}>
                                {activity.description}
                            </td>
                            <td style={{ padding: '10px', border: '1px solid #ccc', width: '10%' }}>
                                {String(activity.done).toLowerCase() === 'true' ? 'Yes' : 'No'}
                            </td>
                            <td style={{ padding: '10px', border: '1px solid #ccc', width: '5%' }}>
                                {/* Add Done button */}
                                <button
                                    className="btn btn-success"
                                    onClick={() => handleDoneButtonClick(activity.activityId)}
                                    disabled={String(activity.done) === 'true'}
                                >
                                    Done
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
            <div style={{ display: 'flex', alignItems: 'center' }}>
                <button
                    style={{ marginLeft: '260px', marginTop: '10px', background: '#E6E6FA', border: 'none', color: 'black' }}
                    className="btn btn-danger"
                    onClick={checkBlockchainIntegrity}
                >
                    Check Blockchain Integrity
                </button>
                <p style={{ marginLeft: '10px', marginTop: '20px' }}>{integrityStatus}</p>
            </div>
        </div>
    );
};

export default Home;