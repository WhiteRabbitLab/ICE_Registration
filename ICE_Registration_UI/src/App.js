import React, { useState, useEffect } from 'react';
import { ChevronLeft, Music, User, Edit2, Check, X, Plus } from 'lucide-react';

// API configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API functions
const fetchArtists = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/artists`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching artists:', error);
    throw error;
  }
};

const fetchArtistTracks = async (artistId) => {
  try {
    const response = await fetch(`${API_BASE_URL}/artists/${artistId}/tracks`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching tracks:', error);
    throw error;
  }
};

const fetchFeaturedArtist = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/artists/featured`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching featured artist:', error);
    throw error;
  }
};

const updateArtist = async (artistId, artistData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/artists/${artistId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(artistData),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error updating artist:', error);
    throw error;
  }
};

const fetchGenres = async () => {
  try {
    const response = await fetch(`${API_BASE_URL}/genres`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching genres:', error);
    throw error;
  }
};

const createTrack = async (trackData) => {
  try {
    const response = await fetch(`${API_BASE_URL}/tracks`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(trackData),
    });
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error creating track:', error);
    throw error;
  }
};

// Artist Selection Page Component
const ArtistSelectionPage = ({ onArtistSelect }) => {
  const [artists, setArtists] = useState([]);
  const [featuredArtist, setFeaturedArtist] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        setError(null);
        const [artistData, featuredData] = await Promise.all([
          fetchArtists(),
          fetchFeaturedArtist()
        ]);
        setArtists(artistData);
        setFeaturedArtist(featuredData);
      } catch (err) {
        setError('Failed to load artists. Please try again later.');
        console.error('Error loading artists:', err);
      } finally {
        setLoading(false);
      }
    };
    loadData();
  }, []);

  if (loading) {
    return (
        <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: '#0d0d0d' }}>
          <div className="text-lg" style={{ color: '#fdfdfd' }}>Loading artists...</div>
        </div>
    );
  }

  if (error) {
    return (
        <div className="flex items-center justify-center min-h-screen" style={{ backgroundColor: '#0d0d0d' }}>
          <div className="text-center">
            <div className="text-lg mb-4" style={{ color: '#a3022d' }}>{error}</div>
            <button
                onClick={() => window.location.reload()}
                className="px-4 py-2 rounded text-white"
                style={{ backgroundColor: '#17a6b1' }}
            >
              Retry
            </button>
          </div>
        </div>
    );
  }

  return (
      <div className="max-w-6xl mx-auto p-6">
        {/* Hero Section with Logo and Artist of the Day */}
        <div className="mb-8 flex gap-6">
          {/* Logo Section */}
          <div className="w-1/2 rounded-lg overflow-hidden shadow-lg">
            <img
                src="https://www.iceservices.com/wp-content/themes/ice/assets/img/iceservices-logo.png"
                alt="ICE Music Services"
                className="w-full h-32 object-contain"
            />
          </div>

          {/* Artist of the Day Section */}
          {featuredArtist && (
              <div className="w-1/2 rounded-lg overflow-hidden shadow-lg" style={{ background: 'linear-gradient(135deg, #17a6b1 0%, #00699e 100%)' }}>
                <div className="p-6 text-white h-32 flex items-center">
                  <div className="flex items-center space-x-4 w-full">
                    {featuredArtist.photo ? (
                        <img
                            className="h-16 w-16 rounded-full object-cover border-2 border-white"
                            src={featuredArtist.photo}
                            alt={featuredArtist.name}
                            onError={(e) => {
                              e.target.style.display = 'none';
                              e.target.nextSibling.style.display = 'flex';
                            }}
                        />
                    ) : null}
                    <div
                        className="h-16 w-16 rounded-full border-2 border-white flex items-center justify-center"
                        style={{
                          backgroundColor: 'rgba(255,255,255,0.2)',
                          display: featuredArtist.photo ? 'none' : 'flex'
                        }}
                    >
                      <User className="h-8 w-8 text-white" />
                    </div>
                    <div className="flex-1">
                      <div className="text-sm font-medium text-blue-100 mb-1">Artist of the Day</div>
                      <h3 className="text-xl font-bold mb-1">{featuredArtist.name}</h3>
                      <div className="flex items-center text-blue-100 text-sm">
                        <Music className="h-4 w-4 mr-1" />
                        {featuredArtist.trackCount || 0} tracks
                      </div>
                    </div>
                    <button
                        onClick={() => onArtistSelect(featuredArtist)}
                        className="px-4 py-2 bg-white bg-opacity-20 hover:bg-opacity-30 rounded-md transition-colors text-sm font-medium"
                    >
                      View Artist
                    </button>
                  </div>
                </div>
              </div>
          )}
        </div>

        {/* Title Section */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2" style={{ color: '#fdfdfd' }}>All Artists</h1>
        </div>

        {artists.length === 0 ? (
            <div className="text-center py-8" style={{ color: '#fdfdfd' }}>
              No artists found.
            </div>
        ) : (
            <div className="rounded-lg shadow-md overflow-hidden" style={{ backgroundColor: '#fdfdfd' }}>
              <table className="w-full">
                <thead style={{ backgroundColor: '#17a6b1' }}>
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Artist</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Tracks</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Description</th>
                </tr>
                </thead>
                <tbody className="divide-y" style={{ backgroundColor: '#fdfdfd', borderColor: '#17a6b1' }}>
                {artists.map((artist) => (
                    <tr
                        key={artist.id}
                        onClick={() => onArtistSelect(artist)}
                        className="cursor-pointer transition-colors hover:opacity-90"
                        style={{ backgroundColor: '#fdfdfd' }}
                        onMouseEnter={(e) => e.target.closest('tr').style.backgroundColor = '#17a6b1'}
                        onMouseLeave={(e) => e.target.closest('tr').style.backgroundColor = '#fdfdfd'}
                    >
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center">
                          {artist.photo ? (
                              <img
                                  className="h-12 w-12 rounded-full object-cover"
                                  src={artist.photo}
                                  alt={artist.name}
                                  onError={(e) => {
                                    e.target.style.display = 'none';
                                    e.target.nextSibling.style.display = 'flex';
                                  }}
                              />
                          ) : null}
                          <div
                              className="h-12 w-12 rounded-full flex items-center justify-center mr-4"
                              style={{
                                backgroundColor: '#17a6b1',
                                display: artist.photo ? 'none' : 'flex'
                              }}
                          >
                            <User className="h-6 w-6" style={{ color: '#fdfdfd' }} />
                          </div>
                          <div className="ml-4">
                            <div className="text-sm font-medium" style={{ color: '#0d0d0d' }}>{artist.name}</div>
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="flex items-center text-sm" style={{ color: '#00699e' }}>
                          <Music className="h-4 w-4 mr-1" />
                          {artist.trackCount || 0} tracks
                        </div>
                      </td>
                      <td className="px-6 py-4">
                        <div className="text-sm max-w-md" style={{ color: '#0d0d0d' }}>
                          {artist.description || 'No description available'}
                        </div>
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </div>
        )}
      </div>
  );
};

// Add Track Form Component
const AddTrackForm = ({ artist, genres, onClose, onTrackAdded }) => {
  const [formData, setFormData] = useState({
    title: '',
    genreId: '',
    lengthSeconds: 0,
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.title.trim()) {
      setError('Track title is required');
      return;
    }
    if (!formData.genreId) {
      setError('Please select a genre');
      return;
    }

    try {
      setSaving(true);
      setError(null);

      const trackData = {
        title: formData.title.trim(),
        genreId: parseInt(formData.genreId),
        lengthSeconds: formData.lengthSeconds,
        artistIds: [artist.id]
      };

      await createTrack(trackData);
      onTrackAdded();
      onClose();
    } catch (err) {
      setError('Failed to create track');
      console.error('Error creating track:', err);
    } finally {
      setSaving(false);
    }
  };

  const handleTimeChange = (minutes, seconds) => {
    const totalSeconds = (parseInt(minutes) || 0) * 60 + (parseInt(seconds) || 0);
    setFormData(prev => ({ ...prev, lengthSeconds: totalSeconds }));
  };

  const formatTime = (totalSeconds) => {
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return { minutes, seconds };
  };

  const { minutes, seconds } = formatTime(formData.lengthSeconds);

  return (
      <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
        <div className="bg-white rounded-lg p-6 w-full max-w-md">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-lg font-semibold" style={{ color: '#0d0d0d' }}>
              Add New Track for {artist.name}
            </h3>
            <button
                onClick={onClose}
                className="p-2 hover:bg-gray-100 rounded-full"
                disabled={saving}
            >
              <X className="h-5 w-5" style={{ color: '#0d0d0d' }} />
            </button>
          </div>

          {error && (
              <div className="mb-4 p-3 rounded text-sm" style={{ backgroundColor: '#fee', color: '#a3022d' }}>
                {error}
              </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: '#0d0d0d' }}>
                Track Title *
              </label>
              <input
                  type="text"
                  value={formData.title}
                  onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2"
                  style={{ borderColor: '#17a6b1', focusRingColor: '#17a6b1' }}
                  placeholder="Enter track title"
                  disabled={saving}
                  autoFocus
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: '#0d0d0d' }}>
                Genre *
              </label>
              <select
                  value={formData.genreId}
                  onChange={(e) => setFormData(prev => ({ ...prev, genreId: e.target.value }))}
                  className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2"
                  style={{ borderColor: '#17a6b1' }}
                  disabled={saving}
              >
                <option value="">Select a genre</option>
                {genres.map((genre) => (
                    <option key={genre.id} value={genre.id}>
                      {genre.name}
                    </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1" style={{ color: '#0d0d0d' }}>
                Length
              </label>
              <div className="flex gap-2 items-center">
                <input
                    type="number"
                    min="0"
                    max="59"
                    value={minutes}
                    onChange={(e) => handleTimeChange(e.target.value, seconds)}
                    className="w-20 px-3 py-2 border rounded-md focus:outline-none focus:ring-2"
                    style={{ borderColor: '#17a6b1' }}
                    placeholder="0"
                    disabled={saving}
                />
                <span style={{ color: '#0d0d0d' }}>min</span>
                <input
                    type="number"
                    min="0"
                    max="59"
                    value={seconds}
                    onChange={(e) => handleTimeChange(minutes, e.target.value)}
                    className="w-20 px-3 py-2 border rounded-md focus:outline-none focus:ring-2"
                    style={{ borderColor: '#17a6b1' }}
                    placeholder="0"
                    disabled={saving}
                />
                <span style={{ color: '#0d0d0d' }}>sec</span>
              </div>
            </div>

            <div className="flex gap-3 pt-4">
              <button
                  type="submit"
                  disabled={saving}
                  className="flex-1 py-2 px-4 rounded text-white font-medium disabled:opacity-50"
                  style={{ backgroundColor: '#17a6b1' }}
              >
                {saving ? 'Creating...' : 'Create Track'}
              </button>
              <button
                  type="button"
                  onClick={onClose}
                  disabled={saving}
                  className="flex-1 py-2 px-4 rounded border font-medium"
                  style={{ borderColor: '#17a6b1', color: '#17a6b1' }}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
  );
};

// Artist View Page Component
const ArtistViewPage = ({ artist, onBack, onArtistUpdate }) => {
  const [tracks, setTracks] = useState([]);
  const [genres, setGenres] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [editedName, setEditedName] = useState(artist.name);
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState(null);
  const [showAddTrackForm, setShowAddTrackForm] = useState(false);

  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        setError(null);
        const [trackData, genreData] = await Promise.all([
          fetchArtistTracks(artist.id),
          fetchGenres()
        ]);
        setTracks(trackData);
        setGenres(genreData);
      } catch (err) {
        setError('Failed to load data.');
        console.error('Error loading data:', err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [artist.id]);

  const handleSave = async () => {
    if (!editedName.trim()) {
      setSaveError('Artist name cannot be empty');
      return;
    }

    try {
      setSaving(true);
      setSaveError(null);
      const updatedArtist = await updateArtist(artist.id, { name: editedName.trim() });
      onArtistUpdate(updatedArtist);
      setIsEditing(false);
    } catch (err) {
      setSaveError('Failed to save changes');
      console.error('Error saving artist:', err);
    } finally {
      setSaving(false);
    }
  };

  const handleCancel = () => {
    setEditedName(artist.name);
    setIsEditing(false);
    setSaveError(null);
  };

  const handleTrackAdded = async () => {
    // Refresh tracks list
    try {
      const trackData = await fetchArtistTracks(artist.id);
      setTracks(trackData);
    } catch (err) {
      console.error('Error refreshing tracks:', err);
    }
  };

  return (
      <div className="max-w-6xl mx-auto p-6">
        {/* Back button */}
        <button
            onClick={onBack}
            className="flex items-center hover:opacity-80 mb-6 transition-colors"
            style={{ color: '#17a6b1' }}
        >
          <ChevronLeft className="h-5 w-5 mr-1" />
          Back to Artists
        </button>

        {/* Artist Banner */}
        <div className="rounded-lg p-8 mb-8 text-white" style={{ background: 'linear-gradient(to right, #17a6b1, #00699e)' }}>
          <div className="flex items-center">
            {artist.photo ? (
                <img
                    className="h-24 w-24 rounded-full object-cover border-4 border-white"
                    src={artist.photo}
                    alt={artist.name}
                    onError={(e) => {
                      e.target.style.display = 'none';
                      e.target.nextSibling.style.display = 'flex';
                    }}
                />
            ) : null}
            <div
                className="h-24 w-24 rounded-full border-4 border-white flex items-center justify-center mr-6"
                style={{
                  backgroundColor: 'rgba(255,255,255,0.2)',
                  display: artist.photo ? 'none' : 'flex'
                }}
            >
              <User className="h-12 w-12 text-white" />
            </div>
            <div className="ml-6">
              <div className="flex items-center mb-2">
                {isEditing ? (
                    <div className="flex items-center gap-2 flex-1">
                      <input
                          type="text"
                          value={editedName}
                          onChange={(e) => setEditedName(e.target.value)}
                          className="text-4xl font-bold bg-white bg-opacity-20 text-white px-3 py-1 rounded border-2 border-white border-opacity-50 focus:border-opacity-100 focus:outline-none"
                          style={{ minWidth: '300px' }}
                          autoFocus
                      />
                      <button
                          onClick={handleSave}
                          disabled={saving}
                          className="p-2 rounded-full bg-white bg-opacity-20 hover:bg-opacity-30 transition-colors disabled:opacity-50"
                      >
                        <Check className="h-5 w-5 text-white" />
                      </button>
                      <button
                          onClick={handleCancel}
                          disabled={saving}
                          className="p-2 rounded-full bg-white bg-opacity-20 hover:bg-opacity-30 transition-colors disabled:opacity-50"
                      >
                        <X className="h-5 w-5 text-white" />
                      </button>
                    </div>
                ) : (
                    <div className="flex items-center gap-3">
                      <h1 className="text-4xl font-bold">{artist.name}</h1>
                      <button
                          onClick={() => setIsEditing(true)}
                          className="p-2 rounded-full bg-white bg-opacity-20 hover:bg-opacity-30 transition-colors"
                      >
                        <Edit2 className="h-5 w-5 text-white" />
                      </button>
                    </div>
                )}
              </div>
              {saveError && (
                  <div className="text-red-200 text-sm mb-2">{saveError}</div>
              )}
              <div className="flex items-center text-blue-100 mb-3">
                <Music className="h-5 w-5 mr-2" />
                {tracks.length} tracks
              </div>
              <p className="text-blue-100 max-w-2xl">
                {artist.description || 'No description available'}
              </p>
            </div>
          </div>
        </div>

        {/* Tracks Section */}
        <div className="rounded-lg shadow-md overflow-hidden" style={{ backgroundColor: '#fdfdfd' }}>
          <div className="px-6 py-4 border-b flex justify-between items-center" style={{ backgroundColor: '#17a6b1', borderColor: '#00699e' }}>
            <h2 className="text-xl font-semibold" style={{ color: '#fdfdfd' }}>Tracks</h2>
            <button
                onClick={() => setShowAddTrackForm(true)}
                className="flex items-center gap-2 px-3 py-1 rounded bg-white bg-opacity-20 hover:bg-opacity-30 transition-colors"
                disabled={loading}
            >
              <Plus className="h-4 w-4" style={{ color: '#fdfdfd' }} />
              <span className="text-sm" style={{ color: '#fdfdfd' }}>Add Track</span>
            </button>
          </div>

          {loading ? (
              <div className="px-6 py-8 text-center" style={{ color: '#0d0d0d' }}>
                Loading tracks...
              </div>
          ) : error ? (
              <div className="px-6 py-8 text-center" style={{ color: '#a3022d' }}>
                {error}
              </div>
          ) : tracks.length === 0 ? (
              <div className="px-6 py-8 text-center" style={{ color: '#0d0d0d' }}>
                No tracks available for this artist.
                <button
                    onClick={() => setShowAddTrackForm(true)}
                    className="ml-2 text-sm underline"
                    style={{ color: '#17a6b1' }}
                >
                  Add the first track
                </button>
              </div>
          ) : (
              <table className="w-full">
                <thead style={{ backgroundColor: '#17a6b1' }}>
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Track Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Genre</th>
                  <th className="px-6 py-3 text-left text-xs font-medium uppercase tracking-wider" style={{ color: '#fdfdfd' }}>Length</th>
                </tr>
                </thead>
                <tbody className="divide-y" style={{ backgroundColor: '#fdfdfd', borderColor: '#17a6b1' }}>
                {tracks.map((track) => (
                    <tr key={track.id} className="hover:opacity-90"
                        onMouseEnter={(e) => e.target.closest('tr').style.backgroundColor = '#17a6b1'}
                        onMouseLeave={(e) => e.target.closest('tr').style.backgroundColor = '#fdfdfd'}>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-sm font-medium" style={{ color: '#0d0d0d' }}>{track.title}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                    <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                          style={{ backgroundColor: '#a3022d', color: '#fdfdfd' }}>
                      {track.genre}
                    </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm" style={{ color: '#00699e' }}>
                        {track.formattedLength}
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
          )}
        </div>

        {/* Add Track Form Modal */}
        {showAddTrackForm && (
            <AddTrackForm
                artist={artist}
                genres={genres}
                onClose={() => setShowAddTrackForm(false)}
                onTrackAdded={handleTrackAdded}
            />
        )}
      </div>
  );
};

// Main App Component
const App = () => {
  const [currentView, setCurrentView] = useState('artists');
  const [selectedArtist, setSelectedArtist] = useState(null);

  const handleArtistSelect = (artist) => {
    setSelectedArtist(artist);
    setCurrentView('artist-detail');
  };

  const handleBackToArtists = () => {
    setCurrentView('artists');
    setSelectedArtist(null);
  };

  const handleArtistUpdate = (updatedArtist) => {
    setSelectedArtist(updatedArtist);
  };

  return (
      <div className="min-h-screen" style={{ backgroundColor: '#0d0d0d' }}>
        {currentView === 'artists' ? (
            <ArtistSelectionPage onArtistSelect={handleArtistSelect} />
        ) : (
            <ArtistViewPage
                artist={selectedArtist}
                onBack={handleBackToArtists}
                onArtistUpdate={handleArtistUpdate}
            />
        )}
      </div>
  );
};

export default App;