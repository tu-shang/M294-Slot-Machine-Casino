const symbols = ['🍒', '🍋', '💎', '🔔', '⭐', '🍉'];
const reels = [
  document.getElementById('reel1'),
  document.getElementById('reel2'),
  document.getElementById('reel3')
];
const result = document.getElementById('result');
const lever = document.getElementById('lever');
const spinButton = document.getElementById('spinButton');

let spinning = false;

function getRandomSymbol() {
  return symbols[Math.floor(Math.random() * symbols.length)];
}

function spinReels() {
  if (spinning) return;
  spinning = true;
  result.textContent = '';
  reels.forEach(r => r.classList.remove('win'));

  const final = [];

  reels.forEach((reel, index) => {
    let i = 0;
    const interval = setInterval(() => {
      reel.textContent = getRandomSymbol();
      i++;
      if (i > 15 + index * 10) {
        clearInterval(interval);
        const symbol = getRandomSymbol();
        reel.textContent = symbol;
        final.push(symbol);
        if (final.length === reels.length) {
          spinning = false;
          checkWin(final);
        }
      }
    }, 80);
  });
}

function checkWin(symbols) {
  if (symbols.every(s => s === symbols[0])) {
    result.textContent = `🎉 JACKPOT mit ${symbols[0]}!`;
    reels.forEach(r => r.classList.add('win'));
  } else {
    result.textContent = 'Versuch es nochmal!';
  }
}

spinButton.addEventListener('click', () => {
  lever.style.transform = 'translateY(60px)';
  setTimeout(() => {
    lever.style.transform = 'translateY(0)';
    spinReels();
  }, 300);
});

lever.addEventListener('click', () => {
  lever.style.transform = 'translateY(60px)';
  setTimeout(() => {
    lever.style.transform = 'translateY(0)';
    spinReels();
  }, 300);
});
