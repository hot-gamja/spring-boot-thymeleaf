-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on username for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);

-- Insert sample data
INSERT INTO users (username, email, full_name) VALUES
    ('john_doe', 'john@example.com', 'John Doe'),
    ('jane_smith', 'jane@example.com', 'Jane Smith'),
    ('bob_wilson', 'bob@example.com', 'Bob Wilson')
ON CONFLICT (username) DO NOTHING;

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Create photos table
CREATE TABLE IF NOT EXISTS photos (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    tags VARCHAR(500),
    genre VARCHAR(100),
    color VARCHAR(50),
    shot_date DATE,
    image_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on title for faster lookups
CREATE INDEX IF NOT EXISTS idx_photos_title ON photos(title);

-- Create trigger to automatically update updated_at for photos
DROP TRIGGER IF EXISTS update_photos_updated_at ON photos;
CREATE TRIGGER update_photos_updated_at
    BEFORE UPDATE ON photos
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Insert sample photo data
INSERT INTO photos (title, description, tags, genre, color, shot_date, image_path) VALUES
    ('Sunset Over Mountains', 'Beautiful sunset view over mountain peaks with golden hour lighting', 'sunset,mountains,nature,landscape', 'Landscape', 'Orange', '2024-08-15', '/home/hot-gamja/uploads/photos/photo-1.jpg'),
    ('Urban Architecture', 'Modern city building with unique geometric design and glass facade', 'architecture,city,modern,building', 'Architecture', 'Blue', '2024-09-20', '/home/hot-gamja/uploads/photos/photo-2.jpg'),
    ('Forest Path', 'Serene forest trail surrounded by tall trees and lush greenery', 'forest,nature,trees,path', 'Nature', 'Green', '2024-07-10', '/home/hot-gamja/uploads/photos/photo-3.jpg'),
    ('Ocean Waves', 'Powerful ocean waves crashing against rocky coastline', 'ocean,sea,waves,coast', 'Seascape', 'Blue', '2024-06-25', '/home/hot-gamja/uploads/photos/photo-4.jpg'),
    ('Vintage Coffee Shop', 'Cozy vintage coffee shop interior with wooden furniture and warm lighting', 'coffee,interior,vintage,cafe', 'Interior', 'Brown', '2024-10-05', '/home/hot-gamja/uploads/photos/photo-5.jpg'),
    ('Desert Landscape', 'Vast desert landscape with sand dunes under clear blue sky', 'desert,sand,landscape,nature', 'Landscape', 'Yellow', '2024-05-18', '/home/hot-gamja/uploads/photos/photo-6.jpg'),
    ('City Night Lights', 'Vibrant city skyline at night with illuminated skyscrapers', 'city,night,lights,skyline', 'Urban', 'Purple', '2024-11-01', '/home/hot-gamja/uploads/photos/photo-7.jpg'),
    ('Autumn Leaves', 'Colorful autumn leaves covering the ground in a park', 'autumn,leaves,fall,nature', 'Nature', 'Red', '2024-10-15', '/home/hot-gamja/uploads/photos/photo-8.jpg'),
    ('Mountain Lake', 'Crystal clear mountain lake reflecting surrounding peaks', 'lake,mountains,water,reflection', 'Landscape', 'Blue', '2024-08-30', '/home/hot-gamja/uploads/photos/photo-9.jpg'),
    ('Winter Wonderland', 'Snow-covered pine trees in a peaceful winter forest scene', 'winter,snow,forest,trees', 'Nature', 'White', '2024-12-10', '/home/hot-gamja/uploads/photos/photo-10.jpg')
ON CONFLICT DO NOTHING;