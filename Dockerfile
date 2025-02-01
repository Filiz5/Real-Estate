# Use the official Node.js image as the base image
FROM node:20.14

# Set the working directory in the container
WORKDIR /app

# Copy package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy the .env.local file to the container
COPY .env.local ./

# Copy the rest of the application code
COPY . .

COPY start.sh ./start.sh
RUN chmod +x start.sh

# Build the Next.js app
RUN npm run build

# Expose the port that Next.js will run on
EXPOSE ${PORT}

# Define environment variables for Next.js

# Start the Next.js app
CMD ["./start.sh"]