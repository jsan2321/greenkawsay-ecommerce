-- CREATE SCHEMA IF NOT EXISTS greenkawsay;
SET search_path TO greenkawsay, public;

-- Extension for UUID
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Category table: Stores product categories for organization and navigation
-- Purpose: Enables hierarchical categorization of products (e.g., Zero Waste, Organic)
-- Relationships: Self-referential for parent categories, referenced by products
CREATE TABLE IF NOT EXISTS categories (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            name VARCHAR(100) NOT NULL,
                            slug VARCHAR(100) UNIQUE NOT NULL,
                            description TEXT,
                            parent_id UUID REFERENCES categories(id),
                            created_by UUID,
                            updated_by UUID,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User profiles table: Stores user information linked to Keycloak authentication
-- Purpose: Manages user data, roles, and impact scores for the e-commerce platform
-- Relationships: Referenced by products, orders, reviews, wishlists, addresses
-- Business Logic: Roles determine permissions, impact_score tracks sustainability contributions
CREATE TABLE IF NOT EXISTS user_profiles (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               keycloak_id VARCHAR(255) UNIQUE NOT NULL,
                               email VARCHAR(255) UNIQUE NOT NULL,
                               first_name VARCHAR(100) NOT NULL,
                               last_name VARCHAR(100) NOT NULL,
                               avatar_url TEXT,
                               role VARCHAR(20) DEFAULT 'customer' CHECK (role IN ('customer', 'vendor', 'admin')),
                               impact_score_total DECIMAL(10,2) DEFAULT 0,
                               is_active BOOLEAN DEFAULT TRUE,
                               created_by UUID,
                               updated_by UUID,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table: Core table for e-commerce items
-- Purpose: Stores product details, pricing, and inventory for the marketplace
-- Relationships: Belongs to categories and user_profiles (vendors), has many product_images, referenced by orders, reviews, wishlists
-- Business Logic: Stock management, pricing, active status for availability
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    category_id UUID REFERENCES categories(id),
    user_id UUID REFERENCES user_profiles(id),
    stock_quantity INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_by UUID,
    updated_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Product images table: Stores multiple images per product
-- Purpose: Supports product visualization with primary image designation
-- Relationships: Belongs to products, cascade delete ensures cleanup
-- Business Logic: One primary image per product for listings
CREATE TABLE IF NOT EXISTS product_images (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_by UUID,
    updated_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Addresses table: User shipping and billing addresses
-- Purpose: Manages delivery locations for orders
-- Relationships: Belongs to user_profiles, referenced by orders
-- Business Logic: One default address per user, supports international shipping
CREATE TABLE IF NOT EXISTS addresses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES user_profiles(id) ON DELETE CASCADE,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100) DEFAULT 'Peru',
    is_default BOOLEAN DEFAULT FALSE,
    created_by UUID,
    updated_by UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_user_profiles_email ON user_profiles(email);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_user_id ON products(user_id);
CREATE INDEX IF NOT EXISTS idx_product_images_product_id ON product_images(product_id);
CREATE INDEX IF NOT EXISTS idx_addresses_user_id ON addresses(user_id);

-- Insert initial categories
INSERT INTO categories (name, slug, description) VALUES ('Zero Waste', 'zero-waste', 'Products that help reduce waste'),
                                                        ('Organic', 'organic', 'Products grown without pesticides or chemicals'),
                                                        ('Fair Trade', 'fair-trade', 'Products that guarantee fair working conditions'),
                                                        ('Local', 'local', 'Products manufactured locally in Peru'),
                                                        ('Recycled Materials', 'recycled-materials', 'Products made from recycled materials')
ON CONFLICT (slug) DO NOTHING;