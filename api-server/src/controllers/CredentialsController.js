const CredentialsService = require('../services/CredentialsService');
const responseBuilder = require('../utils/responseBuilder');
const config = require('../config/config');
const path = require('path');

const credentialsService = new CredentialsService(
  path.join(__dirname, '../../', config.credentialsFilePath)
);

const getCredentials = async (req, res) => {
  try {
    const credentials = await credentialsService.fetchCredentials();
    responseBuilder(res, 200, credentials);
  } catch (error) {
    console.error("Error fetching credentials:", error.message);
    responseBuilder(res, 500, null);
  }
};

module.exports = { getCredentials };