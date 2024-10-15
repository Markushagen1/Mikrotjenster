import React, { useEffect, useState } from 'react';
import apiClient from '../apiClient';

interface UserProfile {
    userId: number;
    name: string;
    age: number;
    budget: number;
    interests: string;
    occupation: string;
}

const ProfileList: React.FC = () => {
    const [profiles, setProfiles] = useState<UserProfile[]>([]);

    useEffect(() => {
        const fetchProfiles = async () => {
            try {
                const response = await apiClient.get('/');
                setProfiles(response.data);
            } catch (error) {
                console.error('Error fetching profiles', error);
            }
        };

        fetchProfiles();
    }, []);

    return (
        <div>
            <h2>Profile List</h2>
            <ul>
                {profiles.map((profile) => (
                    <li key={profile.userId}>
                        <p>Name: {profile.name}</p>
                        <p>Age: {profile.age}</p>
                        <p>Budget: {profile.budget}</p>
                        <p>Interests: {profile.interests}</p>
                        <p>Occupation: {profile.occupation}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProfileList;
