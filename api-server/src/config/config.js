const dotenv = require('dotenv');
dotenv.config();

module.exports = {
  port: process.env.PORT || 3000,
  credentialsFilePath: process.env.CREDENTIALS_FILE_PATH || './credentials.txt',
};