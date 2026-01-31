import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login, register } from '../services/auth';

const Login = () => {
      const [isLogin, setIsLogin] = useState(true);
      const [credentials, setCredentials] = useState({
            username: '',
            email: '',
            password: ''
      });
      const [error, setError] = useState('');
      const [success, setSuccess] = useState('');
      const navigate = useNavigate();

      const handleChange = (e) => {
            const { name, value } = e.target;
            setCredentials(prev => ({
                  ...prev,
                  [name]: value
            }));
      };

      const handleSubmit = async (e) => {
            e.preventDefault();
            setError('');
            setSuccess('');
            try {
                  if (isLogin) {
                        await login(credentials.username, credentials.password);
                        navigate('/');
                  } else {
                        await register(credentials.username, credentials.email, credentials.password);
                        setSuccess('Registration successful! Please login.');
                        setIsLogin(true);
                        setCredentials(prev => ({ ...prev, password: '' }));
                  }
            } catch (err) {
                  console.error('Auth failed:', err);
                  setError(err.response?.data?.message || (isLogin ? 'Invalid credentials' : 'Registration failed'));
            }
      };

      return (
            <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100" style={{ height: '100vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', backgroundColor: '#f3f4f6' }}>
                  <div className="w-full max-w-md p-8 space-y-6 bg-white rounded shadow-md" style={{ width: '100%', maxWidth: '400px', padding: '2rem', backgroundColor: 'white', borderRadius: '8px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}>
                        <h2 className="text-2xl font-bold text-center" style={{ textAlign: 'center', marginBottom: '1.5rem', fontSize: '1.5rem', fontWeight: 'bold' }}>
                              {isLogin ? 'Login to TransitConnect' : 'Sign Up for TransitConnect'}
                        </h2>
                        {error && <div style={{ color: 'red', marginBottom: '1rem', textAlign: 'center' }}>{error}</div>}
                        {success && <div style={{ color: 'green', marginBottom: '1rem', textAlign: 'center' }}>{success}</div>}

                        <form className="space-y-4" onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                              <div>
                                    <label className="block text-sm font-medium text-gray-700" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500' }}>Username</label>
                                    <input
                                          type="text"
                                          name="username"
                                          value={credentials.username}
                                          onChange={handleChange}
                                          required
                                          style={{ width: '100%', padding: '0.5rem', border: '1px solid #d1d5db', borderRadius: '4px' }}
                                    />
                              </div>

                              {!isLogin && (
                                    <div>
                                          <label className="block text-sm font-medium text-gray-700" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500' }}>Email</label>
                                          <input
                                                type="email"
                                                name="email"
                                                value={credentials.email}
                                                onChange={handleChange}
                                                required
                                                style={{ width: '100%', padding: '0.5rem', border: '1px solid #d1d5db', borderRadius: '4px' }}
                                          />
                                    </div>
                              )}

                              <div>
                                    <label className="block text-sm font-medium text-gray-700" style={{ display: 'block', marginBottom: '0.5rem', fontWeight: '500' }}>Password</label>
                                    <input
                                          type="password"
                                          name="password"
                                          value={credentials.password}
                                          onChange={handleChange}
                                          required
                                          style={{ width: '100%', padding: '0.5rem', border: '1px solid #d1d5db', borderRadius: '4px' }}
                                    />
                              </div>
                              <button
                                    type="submit"
                                    style={{ width: '100%', padding: '0.75rem', backgroundColor: '#2563eb', color: 'white', border: 'none', borderRadius: '4px', fontWeight: 'bold', cursor: 'pointer' }}
                              >
                                    {isLogin ? 'Sign In' : 'Sign Up'}
                              </button>
                        </form>

                        <div style={{ marginTop: '1rem', textAlign: 'center' }}>
                              <button
                                    onClick={() => { setIsLogin(!isLogin); setError(''); setSuccess(''); }}
                                    style={{ background: 'none', border: 'none', color: '#2563eb', cursor: 'pointer', textDecoration: 'underline' }}
                              >
                                    {isLogin ? "Don't have an account? Sign Up" : "Already have an account? Login"}
                              </button>
                        </div>
                  </div>
            </div>
      );
};

export default Login;
