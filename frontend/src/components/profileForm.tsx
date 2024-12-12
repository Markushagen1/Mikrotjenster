import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from "../apiClient";
export default function ProfileForm() {
    const [name, setName] = useState('');
    const [age, setAge] = useState('');
    const [budget, setBudget] = useState('');
    const [interests, setInterests] = useState('');
    const [occupation, setOccupation] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const profileData = { name, age, budget, interests, occupation };

        try {
            const response = await apiClient.post('/profiles', profileData);
            const userId = response.data.userId;
            localStorage.setItem('userId', userId); // Lagre bruker-ID
            alert('Profile created successfully and you are now logged in!');
            setName('');
            setAge('');
            setBudget('');
            setInterests('');
            setOccupation('');
            navigate('/');
        } catch (error) {
            console.error('Error creating profile', error);
            alert('Failed to create profile');
        }
    };


    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-b from-purple-50 to-purple-100">
            <div className="bg-white/80 backdrop-blur-sm rounded-lg shadow-lg p-8 w-full max-w-md">
                <h2 className="text-3xl font-bold text-center text-purple-900 mb-8">
                    Registrer Profil
                </h2>
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-gray-700 font-medium mb-2" htmlFor="name">
                            Navn
                        </label>
                        <input
                            type="text"
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="w-full px-4 py-2 border border-purple-300 rounded-lg focus:ring-2 focus:ring-purple-600"
                            placeholder="Ditt navn"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-medium mb-2" htmlFor="age">
                            Alder
                        </label>
                        <input
                            type="number"
                            id="age"
                            value={age}
                            onChange={(e) => setAge(e.target.value)}
                            className="w-full px-4 py-2 border border-purple-300 rounded-lg focus:ring-2 focus:ring-purple-600"
                            placeholder="Din alder"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-medium mb-2" htmlFor="budget">
                            Budsjett
                        </label>
                        <input
                            type="number"
                            id="budget"
                            value={budget}
                            onChange={(e) => setBudget(e.target.value)}
                            className="w-full px-4 py-2 border border-purple-300 rounded-lg focus:ring-2 focus:ring-purple-600"
                            placeholder="Ditt budsjett"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-medium mb-2" htmlFor="interests">
                            Interesser
                        </label>
                        <input
                            type="text"
                            id="interests"
                            value={interests}
                            onChange={(e) => setInterests(e.target.value)}
                            className="w-full px-4 py-2 border border-purple-300 rounded-lg focus:ring-2 focus:ring-purple-600"
                            placeholder="Dine interesser"
                            required
                        />
                    </div>
                    <div>
                        <label className="block text-gray-700 font-medium mb-2" htmlFor="occupation">
                            Yrke
                        </label>
                        <input
                            type="text"
                            id="occupation"
                            value={occupation}
                            onChange={(e) => setOccupation(e.target.value)}
                            className="w-full px-4 py-2 border border-purple-300 rounded-lg focus:ring-2 focus:ring-purple-600"
                            placeholder="Ditt yrke"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full py-3 mt-6 bg-purple-600 hover:bg-purple-700 text-white font-semibold rounded-lg transition-all duration-300 transform hover:-translate-y-1 shadow-md"
                    >
                        Send inn
                    </button>
                </form>
            </div>
        </div>
    );
}



