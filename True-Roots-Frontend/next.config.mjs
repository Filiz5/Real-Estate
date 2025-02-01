/** @type {import('next').NextConfig} */
const nextConfig = {
  images: {
    domains: ['loremflickr.com'], // Add external image domain here
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'loremflickr.com', // Add the domain here
        pathname: '/**' // Match all images under the domain
      }
    ]
  },
  experimental: {
    serverActions: {
      bodySizeLimit: '25mb' // Increase the global body size limit
    }
  }
};

export default nextConfig;
