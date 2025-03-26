const express = require('express');
const cors = require('cors');
const credentialsRoutes = require('./routes/CredentialsRoutes');
const dotenv = require('dotenv');

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

app.use('/', credentialsRoutes);

module.exports = app;