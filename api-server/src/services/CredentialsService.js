const fs = require('fs').promises;

class CredentialsService {
  
  constructor(filePath) {
    this.filePath = filePath;
  }

  async fetchCredentials() {
    const data = await fs.readFile(this.filePath, 'utf-8');
    const lines = data.trim().split('\n');
    
    const credentials = {};

    lines.forEach(line => {
      const [key, value] = line.split('=');
      credentials[key.trim()] = value.trim();
    });

    return {
      username: credentials.username,
      password: credentials.password
    };
  }
}

module.exports = CredentialsService;