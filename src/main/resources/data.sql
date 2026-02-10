-- 사용자 더미 데이터
INSERT INTO users (id, email, nickname, password, created_at, is_deleted, locked, role) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'test@email.com', 'test', '$2a$12$.AmgP4Hqjw3a3ah5/Wo.AuxJoF1mMlP5y5N4ZNucrz4V.fslF1Koi', NOW(), false, false, 'USER'),
('550e8400-e29b-41d4-a716-446655440002', 'test1@email.com', 'test1', '$2a$12$.AmgP4Hqjw3a3ah5/Wo.AuxJoF1mMlP5y5N4ZNucrz4V.fslF1Koi', NOW(), false, false, 'USER');

-- 관심 키워드 데이터
-- DIGITAL 카테고리
INSERT INTO keywords (name, category, display_order, created_at) VALUES
('게임', 'DIGITAL', 1, NOW()),
('프로그래밍', 'DIGITAL', 2, NOW()),
('AI', 'DIGITAL', 3, NOW()),
('유튜브', 'DIGITAL', 4, NOW()),
('웹툰', 'DIGITAL', 5, NOW());

-- CREATIVE 카테고리
INSERT INTO keywords (name, category, display_order, created_at) VALUES
('그림', 'CREATIVE', 1, NOW()),
('글쓰기', 'CREATIVE', 2, NOW()),
('음악감상', 'CREATIVE', 3, NOW()),
('악기연주', 'CREATIVE', 4, NOW()),
('영화', 'CREATIVE', 5, NOW());

-- LIFESTYLE 카테고리
INSERT INTO keywords (name, category, display_order, created_at) VALUES
('운동', 'LIFESTYLE', 1, NOW()),
('여행', 'LIFESTYLE', 2, NOW()),
('카페', 'LIFESTYLE', 3, NOW()),
('요리', 'LIFESTYLE', 4, NOW()),
('반려동물', 'LIFESTYLE', 5, NOW());

-- HOBBY 카테고리
INSERT INTO keywords (name, category, display_order, created_at) VALUES
('사진', 'HOBBY', 1, NOW()),
('독서', 'HOBBY', 2, NOW()),
('등산', 'HOBBY', 3, NOW()),
('캠핑', 'HOBBY', 4, NOW()),
('산책', 'HOBBY', 5, NOW());

-- MUSIC 카테고리
INSERT INTO keywords (name, category, display_order, created_at) VALUES
('K-POP', 'MUSIC', 1, NOW()),
('힙합', 'MUSIC', 2, NOW()),
('인디', 'MUSIC', 3, NOW()),
('재즈', 'MUSIC', 4, NOW()),
('록', 'MUSIC', 5, NOW());

-- LEARNING 카테고리
INSERT INTO keywords (name, category, display_order, created_at) VALUES
('외국어', 'LEARNING', 1, NOW()),
('재테크', 'LEARNING', 2, NOW()),
('자기계발', 'LEARNING', 3, NOW()),
('명상', 'LEARNING', 4, NOW());