import './App.css';
import { useEffect, useState } from 'react';

function App() {
  const [players, setPlayers] = useState([]);
  const [selectedPlayer, setSelectedPlayer] = useState(null);
  const [result, setResult] = useState(null);
  const [newPlayer, setNewPlayer] = useState({ name: '', coins: 1000 });
  const [isSpinning, setIsSpinning] = useState(false);
  const [reels, setReels] = useState(['🍒', '🍋', '⭐']);
  const [leverPulled, setLeverPulled] = useState(false);

  // Spieler laden
  const loadPlayers = () => {
    fetch('http://localhost:8080/player')
      .then(res => res.json())
      .then(data => setPlayers(data));
  };
  

  useEffect(() => {
    loadPlayers();
  }, []);

  // Slot Pull
  const handlePull = () => {
    if (!selectedPlayer || isSpinning) return;

    setLeverPulled(true);
    setTimeout(() => setLeverPulled(false), 300);

    setTimeout(() => {
      setIsSpinning(true);
      setResult(null);

      // Reel Animation
      const symbols = ['🍒', '🍋', '💎', '🔔', '⭐', '🍉'];
      const finalReels = [];
      
      const animateReels = () => {
        let completedReels = 0;
        
        [0, 1, 2].forEach((reelIndex) => {
          let animationCount = 0;
          const interval = setInterval(() => {
            setReels(prev => {
              const newReels = [...prev];
              newReels[reelIndex] = symbols[Math.floor(Math.random() * symbols.length)];
              return newReels;
            });
            
            animationCount++;
            if (animationCount > 15 + reelIndex * 10) {
              clearInterval(interval);
              const finalSymbol = symbols[Math.floor(Math.random() * symbols.length)];
              finalReels[reelIndex] = finalSymbol;
              
              setReels(prev => {
                const newReels = [...prev];
                newReels[reelIndex] = finalSymbol;
                return newReels;
              });
              
              completedReels++;
              if (completedReels === 3) {
                // Animation finished, now call backend
                fetch(`http://localhost:8080/slot/pull/${selectedPlayer.id}`, {
                  method: 'POST'
                })
                  .then(res => res.json())
                  .then(data => {
                    // Verwende die echten Symbole vom Backend
                    if (data.slots) {
                      setReels(data.slots);
                    }
                    setResult(data);
                    setSelectedPlayer(prev => ({ ...prev, coins: data.coins }));
                    setIsSpinning(false);
                  });
              }
            }
          }, 80);
        });
      };
      
      animateReels();
    }, 300);
  };

  const handleAddPlayer = (e) => {
    e.preventDefault();
    if (!newPlayer.name || newPlayer.coins < 0 || newPlayer.coins > 1000) {
      if (newPlayer.coins > 1000) {
        alert('Maximale Anzahl Coins ist 1000!');
      }
      return;
    }

    fetch('http://localhost:8080/player', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newPlayer)
    })
      .then(() => {
        setNewPlayer({ name: '', coins: 1000 });
        loadPlayers();
      });
  };

  const handleDeletePlayer = () => {
    if (!selectedPlayer) return;
    const confirmed = window.confirm(`Spieler "${selectedPlayer.name}" wirklich löschen?`);
    if (!confirmed) return;

    fetch(`http://localhost:8080/player/${selectedPlayer.id}`, {
      method: 'DELETE'
    }).then(() => {
      setSelectedPlayer(null);
      setResult(null);
      loadPlayers();
    });
  };

  const handleRechargePlayer = () => {
    if (!selectedPlayer) return;

    fetch(`http://localhost:8080/player/recharge/${selectedPlayer.id}`, {
      method: 'PUT'
    })
      .then(res => res.json())
      .then(updated => {
        setSelectedPlayer(updated);
        setPlayers(prev => prev.map(p => p.id === updated.id ? updated : p));
      });
  };

  return (
    <div className="app-container">
      {!selectedPlayer ? (
        /* Player Selection Screen */
        <div className="player-selection-screen">
          <div className="casino-header">
            <h1 className="casino-title">CASINO ROYALE</h1>
            <p className="casino-subtitle">Wähle deinen Spieler oder erstelle einen neuen</p>
          </div>

          <div className="player-management">
            <form onSubmit={handleAddPlayer} className="add-player-form">
              <h3>Neuen Spieler erstellen</h3>
              <input
                type="text"
                placeholder="Spielername eingeben..."
                value={newPlayer.name}
                onChange={(e) => setNewPlayer({ ...newPlayer, name: e.target.value })}
                required
              />
              <input
                type="number"
                placeholder="Start-Coins (Standard: 1000)"
                value={newPlayer.coins}
                onChange={(e) => setNewPlayer({ ...newPlayer, coins: parseInt(e.target.value) })}
                min="0"
                max="1000"
                required
              />
              <button type="submit" className="create-player-btn">✨ Spieler erstellen</button>
            </form>

            <div className="player-selection">
              <h3>🎮 Bestehenden Spieler auswählen</h3>
              {players.length > 0 ? (
                <div className="player-list">
                  {players.map(player => (
                    <div 
                      key={player.id} 
                      className="player-card"
                      onClick={() => {
                        setSelectedPlayer(player);
                        setResult(null);
                      }}
                    >
                      <div className="player-info">
                        <h4>{player.name}</h4>
                        <p>{player.coins} 💰 Coins</p>
                      </div>
                      <div className="play-arrow">▶️</div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="no-players">
                  <p>🚫 Keine Spieler vorhanden</p>
                  <p>Erstelle zuerst einen Spieler oben</p>
                </div>
              )}
            </div>
          </div>
        </div>
      ) : (
        /* Slot Machine Game Screen */
        <div className="game-screen">
          {/* Player Info Header */}
          <div className="player-header">
            <div className="current-player">
              <h2>🎮 {selectedPlayer.name}</h2>
              <p className="coin-display">💰 {selectedPlayer.coins} Coins</p>
            </div>
            <button 
              className="back-button"
              onClick={() => {
                setSelectedPlayer(null);
                setResult(null);
                loadPlayers(); // Refresh player list
              }}
            >
              ← Spieler wechseln
            </button>
          </div>

          {/* Slot Machine */}
          <div className="machine">
            <div className="title">🎰 SLOT MACHINE</div>
            <div className="reels">
              {reels.map((symbol, index) => (
                <div 
                  key={index}
                  className={`reel ${result?.win ? 'win' : ''}`}
                >
                  {symbol}
                </div>
              ))}
            </div>
            <button 
              className="spin-button" 
              onClick={handlePull}
              disabled={isSpinning || selectedPlayer.coins < 10}
            >
              {isSpinning ? '🎰 SPINNING...' : '🕹️ PULL (10 Coins)'}
            </button>
            <div className="result">
              {result && (
                result.win
                  ? `🎉 JACKPOT: ${result.winAmount} Coins gewonnen!`
                  : '❌ Kein Gewinn - Versuch es nochmal!'
              )}
            </div>
            <div className="lever-container">
              <div 
                className={`lever ${leverPulled ? 'pulled' : ''}`}
                onClick={handlePull}
              ></div>
            </div>
          </div>

          {/* Player Actions */}
          <div className="player-actions">
            <button className="recharge-btn" onClick={handleRechargePlayer}>
              💳 Coins aufladen (1000)
            </button>
            <button className="delete-btn" onClick={handleDeletePlayer}>
              🗑️ Spieler löschen
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;


