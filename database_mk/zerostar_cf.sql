-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th1 08, 2026 lúc 09:46 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `zerostar_cf`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `auth_tokens`
--

CREATE TABLE `auth_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `auth_hash` char(64) NOT NULL,
  `device_id` varchar(64) NOT NULL,
  `status` enum('ACTIVE','REVOKED','EXPIRED') NOT NULL,
  `expired_at` datetime NOT NULL,
  `last_rotated_at` datetime NOT NULL DEFAULT current_timestamp(),
  `ip_last` varchar(45) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `revoked_at` datetime DEFAULT NULL,
  `revoked_reason` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `auth_tokens`
--

INSERT INTO `auth_tokens` (`id`, `user_id`, `auth_hash`, `device_id`, `status`, `expired_at`, `last_rotated_at`, `ip_last`, `user_agent`, `created_at`, `revoked_at`, `revoked_reason`) VALUES
(28, 9, '5a735b1e9483a9013717c1fb57ff803f6557cdab1ba56d56da168dfdf1d2ae46', 'ebc4ffc61a9ce253f14a0e5f041d016d39efd71104c2c45f6ac32d25fc7a3620', 'REVOKED', '2026-01-04 09:20:10', '2025-12-28 09:20:10', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 09:20:10', '2025-12-28 09:20:23', 'revoked_single'),
(29, 9, '812205b1de6665d19c657dd140c8032276a793dfe4ba8b83d7712c6e77fc9b2b', '398c016f1ce89a1aa3c777cf79993fbc8dab27f22f58644954f09d028dea4b36', 'REVOKED', '2026-01-04 09:39:33', '2025-12-28 09:39:33', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 09:39:33', '2025-12-28 09:39:36', 'revoked_single'),
(30, 9, '855dd53122bc87d4c00649483ac77de0d544120389dc5dd85754edc2be2c4bc9', '3b84d3103505844ec984e87e0baaeac4897656730175f43cffe46e6974f2a888', 'REVOKED', '2026-01-04 09:39:50', '2025-12-28 09:39:50', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 09:39:50', '2025-12-28 09:39:54', 'revoked_single'),
(31, 9, 'fdc4d3aa244c3df39cd44ffa2a680f55088cd9bafa9a9975d62c62c5aae931ac', 'b3f7623baacc8301fa926d9c351777762fd999ab4b8b8761a2f0bc39153c241f', 'REVOKED', '2026-01-04 09:52:56', '2025-12-28 09:52:56', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 09:52:56', '2025-12-28 09:52:58', 'revoked_single'),
(32, 11, '50633dbf1cbfb755182a35b000358986d8400a261d4b0aa1f4ee5e4d24b6625b', '1504d85157e4296f5a731f525b3c4b60aaf6c192bb06958c9222f61334f6e738', 'REVOKED', '2026-01-04 09:53:09', '2025-12-28 09:53:09', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 09:53:09', '2025-12-28 09:53:13', 'revoked_single'),
(33, 9, 'a02a860a449fedd4043c20e3033bbe6b8dd7d21ac131b1b8257d970f0e078bfc', 'b75b778a32f360de480ed509b807f2116b68c705b1588e1bd81048717d7cd9d0', 'REVOKED', '2026-01-04 10:07:38', '2025-12-28 10:07:38', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 10:07:38', '2025-12-28 10:07:50', 'revoked_single'),
(34, 9, '2766860b83cb1e525147a477990cb98f7590ae0f1ef0531fb73092004575ca1f', '6c0302692f9167e17bcf0b9381b0b53058511e94c154b270a31fe06b4ea9fa22', 'REVOKED', '2026-01-04 10:18:27', '2025-12-28 10:19:13', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-28 10:18:27', '2025-12-28 10:19:22', 'revoked_single'),
(35, 9, '6c7ba50f5845c9e802ec856e85d714891b693441dc3eb2ca689aa5108c0680fd', '3fe9cedeeb96b6e061a743874b73e9370c0d97d063c6c5b791ad1e34a834264a', 'REVOKED', '2026-01-06 12:58:35', '2025-12-30 12:58:35', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 12:58:35', '2025-12-30 13:05:24', 'revoked_by_system'),
(36, 9, '470232f4a49067a31b60625a62d398c8484b3645516235e20327c02418cc3db6', 'c65b4859df92b1e126ea310348085ed97fbe7f1a972af8908e24d5b52b75961d', 'REVOKED', '2026-01-06 13:05:24', '2025-12-30 13:17:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 13:05:24', '2025-12-30 13:19:27', 'revoked_by_system'),
(37, 9, '64d10a7a92590e740ee2e1ff131d16bd7c32ed515c25dbbe77f2e6674449bac3', '63ab9e874d8d7fccca9ba44f2d367e9b95f91d5993bc964a16d810f9f0550b35', 'REVOKED', '2026-01-06 13:19:27', '2025-12-30 13:19:27', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 13:19:27', '2025-12-30 13:19:57', 'revoked_single'),
(38, 9, '7fbdbc94ca116a132900620bb1a9951fe7be4c6dfa1df8343ac9f762bff67dd4', '47987258bc5b9c7acb40d220fc8d0a184f54832815967109914de96b155ba01d', 'REVOKED', '2026-01-06 13:20:33', '2025-12-30 13:20:33', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 13:20:33', '2025-12-30 13:23:50', 'revoked_single'),
(39, 9, '422965e348ce1624d20612a1a1f882a4a13132bf3b3a330d6c8565afe4497b7d', '7986a1f19c8a0302048a3cad21f9cbe021e3f3dfb2931e65888bcfbeb0035d4b', 'REVOKED', '2026-01-06 13:25:37', '2025-12-30 14:21:59', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 13:25:37', '2025-12-30 14:23:37', 'revoked_by_system'),
(40, 9, '3ed4888e3c97033937fc1b47461d742d3ada09e7270d787764af248d99b1f770', '64d2c1dc15c5da8cc8141a62fc03b1f4fb7644d69db75e6fc8842ab4f05d03ab', 'REVOKED', '2026-01-06 14:23:37', '2025-12-30 14:23:37', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 14:23:37', '2025-12-30 14:26:53', 'revoked_single'),
(41, 9, 'f74921805851cf5ec05b853aa9eab62db153daa4a14cc25c90697aaf188b06fc', '3ae7446c7f42ec54c718fa211ef6408c20945e1673fca75655d1443a80a0f8b3', 'REVOKED', '2026-01-06 14:32:57', '2025-12-30 14:32:57', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 14:32:57', '2025-12-30 14:37:30', 'revoked_by_system'),
(42, 9, '045125a32168381cbe699ea765005bf282c38cb695c5d9770390fba9ebc18053', '5473df0950de7fd6307008397261024eb65fd4144a25f5db727b0505fb15aa4d', 'REVOKED', '2026-01-06 14:37:30', '2025-12-30 14:37:30', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36', '2025-12-30 14:37:30', '2025-12-30 14:38:40', 'revoked_by_system'),
(43, 9, '5174699623db8aa96fb28bf4a55bf145a336856450009a13616a093d9ec7bd62', '565f0dd0961e87582bcca0f660b6d8d04d95c7d696a3466b01d8ee29c65ca160', 'REVOKED', '2026-01-06 14:38:40', '2025-12-30 14:38:40', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-30 14:38:40', '2025-12-30 14:39:22', 'revoked_by_system'),
(44, 9, '76337284085d16a3b93b5f2bf8d7577b57404d6d3199a6271cdee95a8684d815', '0126e060d4bebb4dbef1de80a63f7750399bea41a743874e701c6ca90d9dcbc9', 'REVOKED', '2026-01-06 14:39:22', '2025-12-30 14:39:22', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-30 14:39:22', '2025-12-30 14:51:45', 'revoked_by_system'),
(45, 9, '7c324098d05329215049b88ee7ad4f21941ce06f1cc7891ee45013408f922588', 'a473fa816741390fe035e0feff6c34881cda0c3ad3fb53d6aaa542c413877d88', 'REVOKED', '2026-01-06 14:51:45', '2025-12-30 14:51:45', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-30 14:51:45', '2025-12-30 14:53:14', 'revoked_by_system'),
(46, 9, '89f70b6bffcfcee9bc8135cfe5464b39eec0b5ac6dc09acd9a971fa5c3769b34', 'b66ec42c06483a6828ad2523df6c62d18f0e09e68fce85a0c497258daf31829d', 'REVOKED', '2026-01-06 14:53:14', '2025-12-30 15:01:36', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-30 14:53:14', '2025-12-30 21:54:31', 'revoked_by_system'),
(47, 9, '915b9288f787dbc1883f4f2a18e6b3e537da9354974c1f8a0dbc7333f4a2c98a', 'ab896cacd338995ab79005067a063183ab2c3c38851d2e27ac01343e67fe513d', 'REVOKED', '2026-01-06 21:54:31', '2025-12-30 21:54:31', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-30 21:54:31', '2025-12-31 18:41:12', 'revoked_by_system'),
(48, 9, '058eb874b29c278ef058927cd7c32486ff4388908ce3068b0651a120fcd10f51', '1e943e0c192bc69067ca25c3ea8f4f9395f7ea0494bdd09ff6ca1f37228b6486', 'REVOKED', '2026-01-07 18:41:12', '2025-12-31 18:41:12', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 18:41:12', '2025-12-31 18:44:51', 'revoked_by_system'),
(49, 9, 'e23a4874790fe23afd74f34a0d8e0ff4303e0f20b9e3e7fdae46c3a0191bc048', '5ebb9d41c8b6f9bd9a0fe5a004c8a292cf25258f82beb3ec23a275098b038690', 'REVOKED', '2026-01-07 18:44:51', '2025-12-31 18:44:51', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 18:44:51', '2025-12-31 18:47:52', 'revoked_by_system'),
(50, 9, '85678597317ac5b44fc0f19890a0e78ed89980899de5369b8a571bd740be02f7', 'c0e5664a9767feb6af4febfcfd0ba3c3825bf5763dff2bbcc238ebee1f600ece', 'REVOKED', '2026-01-07 18:47:52', '2025-12-31 18:47:52', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 18:47:52', '2025-12-31 18:48:14', 'revoked_single'),
(51, 9, 'bc969e941da0ef441af8c0a2688086c0d3b890c715b6a22ca332c3e138b32de7', 'ce86567d9723e566dc1b2fa6f43eebb13a35a368cfaeea7e866c16cc2ada1d97', 'REVOKED', '2026-01-07 19:30:03', '2025-12-31 19:30:03', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:30:03', '2025-12-31 19:33:17', 'revoked_by_system'),
(52, 9, '7c4ecbc14dc23a2b471c38b08ef51aed45835aa2104f515926c3b51999681737', 'ce86567d9723e566dc1b2fa6f43eebb13a35a368cfaeea7e866c16cc2ada1d97', 'REVOKED', '2026-01-07 19:33:17', '2025-12-31 19:33:17', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:33:17', '2025-12-31 19:33:41', 'revoked_by_system'),
(53, 9, '0158826bb98d08bdbd8f176aa221e1c1b2cb008877e3f687f8bac811babf2f07', 'dac976867cc737a523599e6e12f427c28feaf8d63579d0620a264606ff2e8c0a', 'REVOKED', '2026-01-07 19:33:41', '2025-12-31 19:33:41', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:33:41', '2025-12-31 19:34:00', 'revoked_by_system'),
(54, 9, '5302c62600b7bddae3e1538caa614ab0ef10a94e6728093cb8c3973a675403a4', 'dac976867cc737a523599e6e12f427c28feaf8d63579d0620a264606ff2e8c0a', 'REVOKED', '2026-01-07 19:34:00', '2025-12-31 19:34:00', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:34:00', '2025-12-31 19:36:01', 'revoked_by_system'),
(55, 9, '69653f3c7b1c9eb8592d4f53920116161ea35908d4564f4fed97f96ec687261e', 'f4cde95c3c977eb2b56a64ebdd655334f23134ee247425e403a7eceb0b2448b8', 'REVOKED', '2026-01-07 19:36:01', '2025-12-31 19:39:27', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:36:01', '2025-12-31 19:40:29', 'revoked_by_system'),
(56, 9, 'f6403b361bdd6a66780f23e5063dd2ef8da93387d1b8359dc6204ef53d1a433b', '3e21fe6f360786540e0e815b6c3026d4422254f48c1cc97cc6b4742b9dcd8c0f', 'REVOKED', '2026-01-07 19:40:29', '2025-12-31 19:40:29', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:40:29', '2025-12-31 19:40:40', 'revoked_single'),
(57, 9, '81669cd616bc655333e92f32feae58910ed4c58f6d653dcd0c324b226a06867d', '4ba77d8779c28597a8e0bf63863675c1ef50f7b46575c0ed9b03292f8ada9369', 'REVOKED', '2026-01-07 19:40:52', '2025-12-31 19:40:52', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 19:40:52', '2025-12-31 19:40:55', 'revoked_single'),
(58, 9, '42ac624acdf06d1e0e1f12d64ea7f2a67b9673c6863cf5672230126738993caa', '97346170e09a95bffc81199c13e1f3313795611e4b0043120951a2a1c64f73ff', 'REVOKED', '2026-01-07 20:08:29', '2025-12-31 20:08:29', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2025-12-31 20:08:29', '2025-12-31 20:08:33', 'revoked_single'),
(59, 9, 'aca6138e442eb57b32eac5eaa2490ee6bb1e04eb5de569cd7aa40543b1c7339e', '2d30e1b76d755d1f6f93d921890ff99e07d8ec49ebeedd29c324e1a4d7678420', 'REVOKED', '2026-01-08 06:00:15', '2026-01-01 06:00:15', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 06:00:15', '2026-01-01 06:00:19', 'revoked_single'),
(60, 9, 'ab6724df1c58d1376d94480e9889e7e252407726dbcfac54d0ea8a83d51f11fb', '18115558159451ffaaaae32bba58538ac0cd6bbbfc495a08f2fcfce53011c466', 'REVOKED', '2026-01-08 06:02:59', '2026-01-01 06:02:59', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 06:02:59', '2026-01-01 06:03:02', 'revoked_single'),
(61, 9, '4bd7727ea2f3a7a96ec6cf1114f90146be391f5055bafc93594190db5bb047b9', 'ddac034a256a47225ac4a6565eb438d9f6365861e98d9b1605860786a7c1a0a0', 'REVOKED', '2026-01-08 06:04:15', '2026-01-01 06:04:15', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 06:04:15', '2026-01-01 06:04:33', 'revoked_single'),
(62, 9, 'bb27e76e8df918e1903d049aa4bba800974b2dc296e9208fde75f140999e654f', '292c511aa1d326eb951bb35a65c22b3eb71bc5bfb8a989f4bf71f890390805a1', 'REVOKED', '2026-01-08 06:04:49', '2026-01-01 06:04:49', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 06:04:49', '2026-01-01 06:05:48', 'revoked_single'),
(63, 9, 'a0b665f36666815d4387ab2139cbe5c71663266be8660b8104259ce8141e3611', 'b2d03eb629a4e8b016937d99a70d43017f8eca3372c0f53e5a23f06572a12875', 'REVOKED', '2026-01-08 06:06:17', '2026-01-01 06:06:17', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 06:06:17', '2026-01-01 06:09:43', 'revoked_single'),
(64, 9, '3f213e7a1bcac5c43685299ece8561a8df72216fed7f9b6627aa6577ef990aed', 'a1e784c7b9745b7624b692e2c11f931ffe1087305fe126ca47c11173f0607081', 'REVOKED', '2026-01-08 08:36:13', '2026-01-01 08:41:31', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 08:36:13', '2026-01-01 08:41:37', 'revoked_single'),
(65, 9, 'f9fcb2ac3efd7ce1d4d012c989be0b373dfe264ddab1a974bdc9b259a404def9', 'c01ef76fe06ec1b5d9e13e901594bc57a4217a2d728e8d3b5bc8406ff5d6b1e1', 'REVOKED', '2026-01-08 08:56:01', '2026-01-01 08:56:01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 08:56:01', '2026-01-01 09:25:46', 'revoked_by_system'),
(66, 9, 'a3d4cb09a41b116c44fccaff03a9d6ce9a5b9c8cd53568cfaae5e347a0b8f2b2', '281c5b8980cfc88cc2c4b0da844fceba2104bbc3ec4221d1109881d18af461ae', 'REVOKED', '2026-01-08 09:25:46', '2026-01-01 09:25:46', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 09:25:46', '2026-01-01 09:27:01', 'revoked_by_system'),
(67, 9, 'd6019185151871d10f6280c3b9df7fd6cb0e1460e7dea964a76f9c7b5572bf6b', '281c5b8980cfc88cc2c4b0da844fceba2104bbc3ec4221d1109881d18af461ae', 'REVOKED', '2026-01-08 09:27:01', '2026-01-01 09:27:01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 09:27:01', '2026-01-01 09:27:33', 'revoked_by_system'),
(68, 9, '7ec1c106a562efa04075881a3110ec97e9790a60f999b7f4aab084035f8cfa71', '1a876cbe7f7568d69eff065159b168c498d74f3fef6fe4b74a7f3a43085af0c9', 'REVOKED', '2026-01-08 09:27:33', '2026-01-01 09:27:33', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 09:27:33', '2026-01-01 09:36:06', 'revoked_single'),
(69, 9, '9581193d902fcd648f95d068aab6192dc5ff65c7e155850b32dc17726899855b', '5fd5c973243651ec2d2e2595e6c5cc2acc98fc0bfb597624460a73afabd7548b', 'REVOKED', '2026-01-08 09:56:20', '2026-01-01 09:56:20', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 09:56:20', '2026-01-01 09:57:23', 'revoked_by_system'),
(70, 9, 'da644c48533f1545835ba2ede94272bc7d7d1cafbd6e2810d173bf58f1640c73', '5fd5c973243651ec2d2e2595e6c5cc2acc98fc0bfb597624460a73afabd7548b', 'REVOKED', '2026-01-08 09:57:23', '2026-01-01 09:57:23', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 09:57:23', '2026-01-01 10:15:56', 'revoked_by_system'),
(71, 9, '0d689486aee355d2789c7b63226b912ea85e5885f5ff094b6f568074a5d416eb', '8e99d7fab4e03e12f88b52f8228ffc9a7c47dd969c5ec9a20a1b90801a0d6e0e', 'REVOKED', '2026-01-08 10:15:56', '2026-01-01 10:15:56', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 10:15:56', '2026-01-01 10:19:18', 'revoked_by_system'),
(72, 9, '7ea6d129d9a3922207a2a69c6d7eaf55ee954d6ff495bbdba37da8b9538c9602', '8e99d7fab4e03e12f88b52f8228ffc9a7c47dd969c5ec9a20a1b90801a0d6e0e', 'REVOKED', '2026-01-08 10:19:18', '2026-01-01 10:19:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 10:19:18', '2026-01-01 10:34:58', 'revoked_by_system'),
(73, 9, '544e28fee385359c211146c50a0c3c5023bf75ada6852a248b5ea62e88f0284a', '3266ae9da24c33574517bcc0582d62aceed851bb96390b8d6e36cf68a948098f', 'REVOKED', '2026-01-08 10:34:58', '2026-01-01 10:34:58', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 10:34:58', '2026-01-01 10:35:08', 'revoked_single'),
(74, 9, 'dda82734f88bc5b1e88bd9b670feb81ff952855b7555ae009cba509ef029f3d2', 'e676ca23ba67b42278d6b79d11f8d1a513f96bf384aade3ae20b2f6362dcf5c1', 'REVOKED', '2026-01-08 10:57:51', '2026-01-01 10:57:51', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 10:57:51', '2026-01-01 10:58:05', 'revoked_single'),
(75, 9, 'db71aba99dd9323beb713a4ef7feda832a5f1df030bbad8bdf68fec554752db3', '0f7f456222d54c7292d225cbe7e4be2b4ed7c6dc3018075c206752494d4f1cf1', 'REVOKED', '2026-01-08 10:58:17', '2026-01-01 10:58:17', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 10:58:17', '2026-01-01 10:58:25', 'revoked_single'),
(76, 9, '7c0e7b23d4b234f9693e7849c9b396deec6d711ef9be80886ccd73691c35c302', 'd80ac29d29d521c81315f27f7385c6401f52a07939884829c6ab742d77adb58e', 'REVOKED', '2026-01-08 10:58:34', '2026-01-01 10:58:34', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 10:58:34', '2026-01-01 11:01:45', 'revoked_by_system'),
(77, 9, 'e97e22b9f0c5b2dbceaddd5858d267d804f2fa32d70ee0d789ea268b9d26a6df', 'e7b43fb3d9c2aa6f67932797d95f36174d8f17a8c4857cb37569189ad4429415', 'REVOKED', '2026-01-08 11:01:45', '2026-01-01 11:01:45', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:01:45', '2026-01-01 11:01:54', 'revoked_single'),
(78, 9, 'd9e05ddf874265ac93113353e7adadb38f3f3676af523fb959653fe4d433c815', 'da5b20bbbdcc1774e77ced30d7897d6c23fd82360f1c9950ab4ccfbd7c75dfbf', 'REVOKED', '2026-01-08 11:02:53', '2026-01-01 11:02:53', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:02:53', '2026-01-01 11:07:03', 'revoked_by_system'),
(79, 9, '646e799fef3d6525fdf60762252be8ee509b9f679aed6b17739163329f06511e', '6843f79f395a1fc0c7af3d1d93d8ba2685003c9ead908f418ac3f33b596ab793', 'REVOKED', '2026-01-08 11:07:03', '2026-01-01 11:07:03', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:07:03', '2026-01-01 11:10:01', 'revoked_by_system'),
(80, 9, '2e6ecd4bc388467f59f740cf3c0671e41e8a72b64dd9f8c20190f7cda89b789f', '6dd1ca6a5351df22b749c02b17b3ec1687750e6cabd5b98ab1f2aaf0bde9b005', 'REVOKED', '2026-01-08 11:10:01', '2026-01-01 11:10:01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:10:01', '2026-01-01 11:10:17', 'revoked_by_system'),
(81, 9, '9fe332852d1bc4b37c5048e04804034444094114bbeb7f1dca63d57604633623', '6dd1ca6a5351df22b749c02b17b3ec1687750e6cabd5b98ab1f2aaf0bde9b005', 'REVOKED', '2026-01-08 11:10:17', '2026-01-01 11:10:17', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:10:17', '2026-01-01 11:11:39', 'revoked_by_system'),
(82, 9, '42bfeb65d455738f49be825dfecf69dc9ed291b4d8cfc41cf335b75e6e58a94d', '375aef212a9b17cb6040d4c3c8c3dfe537ecb76256cb7799b60875a7f7e8540d', 'REVOKED', '2026-01-08 11:11:39', '2026-01-01 11:11:39', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:11:39', '2026-01-01 11:27:34', 'revoked_by_system'),
(83, 9, '8ee1914014d03766314b1054ea4908f55ce0841123cfa163fb22a44825154230', '490a733bd99b8fc8cacb2d4c345aa00e316755b470ba467575fb65e2e21b5c79', 'REVOKED', '2026-01-08 11:27:34', '2026-01-01 11:27:34', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:27:34', '2026-01-01 11:31:16', 'revoked_by_system'),
(84, 9, '32902231e41a2082cb63a26ad4bba2812921fc3490bdb414c15888cfd8990d04', '33fb97ad9ab4ac26aabb1c9901e181266bb0a6e5837ca48b67d4d616041a4b9f', 'REVOKED', '2026-01-08 11:31:16', '2026-01-01 11:31:16', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:31:16', '2026-01-01 11:34:25', 'revoked_by_system'),
(85, 9, 'c4f7831c0f9a290df287a536a896058e5a50abb762316177b87f8a7f7e11497b', '33fb97ad9ab4ac26aabb1c9901e181266bb0a6e5837ca48b67d4d616041a4b9f', 'REVOKED', '2026-01-08 11:34:25', '2026-01-01 11:34:25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:34:25', '2026-01-01 11:37:57', 'revoked_by_system'),
(86, 9, 'ae60c2713eadf3f18d2812ebcd84ba24f0b85cfb1ec66ad44fbba9d7ea52fc2d', '74d3a4e1c5606d7deb441b262841da81d2b2dc408eccba3832c728cdfed249a2', 'REVOKED', '2026-01-08 11:37:57', '2026-01-01 11:37:57', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:37:57', '2026-01-01 11:43:07', 'revoked_by_system'),
(87, 9, 'e211ee60f0a6a856a46162ba4f51fe674c4963536baeab2e8e3983fdc4f8c0e4', 'ce15f511c32c499198925eeb7e56f6ddbfdba7ba5bdc67e59c202ef7640187d9', 'REVOKED', '2026-01-08 11:43:07', '2026-01-01 11:43:07', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:43:07', '2026-01-01 11:47:58', 'revoked_by_system'),
(88, 9, '24732bae053cc093fe183237b66639bf09dc78f44cf6e152042c899d5210f55c', 'b36cc072c3ed49d43ecc6a8b53b05094fcd7caf591a9751184975205ab88ef8f', 'REVOKED', '2026-01-08 11:47:58', '2026-01-01 11:47:58', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:47:58', '2026-01-01 11:50:53', 'revoked_single'),
(89, 9, 'da935537a6fe853da575676b22b4f65f6036250dfb51a3ee0d6ade34c5438766', '88a01bafb5eb537c9a746c376facc159b5138a44dccf6b29dd3fedae80212991', 'REVOKED', '2026-01-08 11:51:04', '2026-01-01 11:51:04', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:51:04', '2026-01-01 11:51:09', 'revoked_single'),
(90, 9, '0de0604737825447d1d0f401d756f4c555aaea6013ccedc716e2024ed0f39328', 'cdd37521bfa3e4b733795e75bd144bb4db62d7d8d8571b261df24e1b9e6e80dd', 'REVOKED', '2026-01-08 11:51:17', '2026-01-01 11:51:17', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:51:17', '2026-01-01 11:52:38', 'revoked_single'),
(91, 9, '94dfaea5d079093c4645beff935109678a49db6c3d67f632bfd4bff997a11776', '880dc9604a6e14f62c7f2995c04e16d80015ce2a7e6ff8edb7a435e2c34eac1a', 'REVOKED', '2026-01-08 11:52:50', '2026-01-01 11:52:50', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:52:50', '2026-01-01 11:52:52', 'revoked_single'),
(92, 9, '6220d6187e374afafb90a9c6d4e24628aac6bbec63f13d8859b00c975baa877f', '367e8ec469670ea19eaf46aa933d5d0b0d42277e6d5f8353f3e100eb2dee9d63', 'REVOKED', '2026-01-08 11:55:13', '2026-01-01 11:55:13', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 11:55:13', '2026-01-01 11:55:21', 'revoked_single'),
(93, 9, 'e952b2a98b42343cd099f236b6f425369d8326b37c0272832b773487ec3c226f', '791667caba1acd0197ea3e158d6a47126eb40cbe120a34916a957a47878a7aec', 'REVOKED', '2026-01-08 12:11:35', '2026-01-01 12:11:35', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 12:11:35', '2026-01-01 12:11:38', 'revoked_single'),
(94, 9, '5f8c071107ad344b19f63eca1d2310b3d58253c091d5df475080d5b24a079487', '30c2c92034dc82fa00aacbb5a7a6811fec5aa076bab0d8c0d6aededaa7f436de', 'REVOKED', '2026-01-08 12:52:06', '2026-01-01 12:52:06', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-01 12:52:06', '2026-01-01 12:52:14', 'revoked_single'),
(95, 9, '5e512e4a9b7aa1a35b857540e8911066fdedb0637a48af8549b30125ab62ff5a', '3e6d66f1382b261e5d23721401e88e5b9ace3d72622c1b56233fbbe43e6cf187', 'REVOKED', '2026-01-09 17:58:01', '2026-01-02 17:58:01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 17:58:01', '2026-01-02 17:58:19', 'revoked_single'),
(96, 9, '6fa6d1b0a04b8baba6750e45bb8fe844a4792261c6290e1117ad0caab949869a', '503b2af50faaa4c6908cbcaccced1bed9c63fee7465a77f1a7d6aabee39b47e7', 'REVOKED', '2026-01-09 20:55:58', '2026-01-02 20:55:58', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 20:55:58', '2026-01-02 20:56:04', 'revoked_single'),
(97, 9, '9cecbe95d1936c5fac070eaf3b30693f37f57e89c4b92e3f78903ab972d077cf', '6cbf12a5510b48e873ab795954b3eb5c786d90e63883271e10b91bbf13602801', 'REVOKED', '2026-01-09 20:56:18', '2026-01-02 20:56:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 20:56:18', '2026-01-02 20:56:36', 'revoked_single'),
(98, 9, 'a55ac6234a4d2edc4c46435ae114fe774c01cbba3574b3923c02dbf30d58961c', '16566875478aafe46d4d728511df109fa994c374d46f9dce75041961764c6645', 'REVOKED', '2026-01-09 21:08:40', '2026-01-02 21:08:40', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 21:08:40', '2026-01-02 21:08:47', 'revoked_single'),
(99, 9, 'b64f446b5e22654525e1ce715f3bb0300f57b3dee3c1fcee42dd96137383ada2', 'f314e3fa0fba0b787a5199fc5e8c232dced03dc1764e31fa674ffb8701aadab0', 'REVOKED', '2026-01-09 21:08:56', '2026-01-02 21:08:56', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 21:08:56', '2026-01-02 21:09:00', 'revoked_single'),
(100, 9, '5e27e549d9bdbc19718aa9e9e6b3217b76d9f5854cc59868f3958f2794c37a64', '8e2c23159a6118a7aaa1c5f93a58a0a02cdc9024b930c198fa2a3689261ee00b', 'REVOKED', '2026-01-09 21:20:14', '2026-01-02 21:50:16', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 21:20:14', '2026-01-02 21:50:16', 'revoked_single'),
(101, 9, 'cfba22563ee291642ca03c079d22d401f7d4198a2c59ded17baf3d382336540c', '87f44b1c70bc4374be99ad62808177e3dfbf8874e3879322db639f20e30862e0', 'REVOKED', '2026-01-09 21:50:40', '2026-01-02 21:50:40', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-02 21:50:40', '2026-01-02 21:50:43', 'revoked_single'),
(102, 9, '01206df6a1d084c8a8178eb689272e2a92e4820086b4d2f2e2e84bf1f221a2e2', '452ea770280a6736d65a162de7bf5607ca1926137c0a0cd62d79c0b5d0607d57', 'REVOKED', '2026-01-10 07:37:15', '2026-01-03 07:37:15', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 07:37:15', '2026-01-03 07:38:03', 'revoked_single'),
(103, 9, '087b035e799c193c8e340de4fe504c7b3df53923b279d77bad5439d41ed69f26', 'd6c836eb344ebef8f5a1b8e1486d59e719eb3211fd9e5f66058a80780fd8fc40', 'REVOKED', '2026-01-10 07:38:17', '2026-01-03 07:49:15', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 07:38:17', '2026-01-03 07:49:48', 'revoked_by_system'),
(104, 9, '5466de808a7f688892f090b270e97703bb626f53d22189479c6ef19bb14accde', 'aa35563cf0bc06966daabf7e3f2518a4a6dd58b07206b05dc898a123745fca0f', 'REVOKED', '2026-01-10 07:49:48', '2026-01-03 07:49:48', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 07:49:48', '2026-01-03 07:53:11', 'revoked_by_system'),
(105, 9, '49e5f1dbebba9cafdf23d6248f7c6a9a3a4c242f946f275748ef6a2a2b6aff1f', '5611b8ee92f378aed7de15c4ce0b9e1f0806fa9374651e06b09cc3c9c10a0fa9', 'REVOKED', '2026-01-10 07:53:11', '2026-01-03 07:55:45', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 07:53:11', '2026-01-03 07:55:48', 'revoked_single'),
(106, 9, 'f182f7cdc6a54134165f6b8e9ce1ab49339c91f8a5ecbdd6fb3e6cd276f201c5', '2173262d39213b325343bcd36d769201c363acf7439e9043a75b8c862b13730b', 'REVOKED', '2026-01-10 07:55:58', '2026-01-03 07:55:58', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 07:55:58', '2026-01-03 07:56:23', 'revoked_by_system'),
(107, 9, '58f8ce62d83412ff0525e69aa4a72d59f20086b788faca7dda990b83393c99fe', 'c9abdfa09cb2130db952fb6236fcf8beecab231e4dc3921463663a711698eccf', 'REVOKED', '2026-01-10 07:56:23', '2026-01-03 07:56:23', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 07:56:23', '2026-01-03 08:01:19', 'revoked_by_system'),
(108, 9, 'b059f7efbedd86dfed23e3de2ebbbaa437d35b677c10afd1579af4177d7e3f6d', '0eef937f868e2472a898279238d3347b7488c06948f05f0b5f4d6a37abd034c4', 'REVOKED', '2026-01-10 08:01:19', '2026-01-03 08:01:19', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:01:19', '2026-01-03 08:02:48', 'revoked_single'),
(109, 9, '6e6b8e747a45b395461f7961e48b46cff5ce0bb5994ce8a20d8e180727958fcd', 'f96d38cfbf634ecbd2dc3acd8d337dd81f72449a5cac49f8ae1d8f3ff341b884', 'REVOKED', '2026-01-10 08:05:48', '2026-01-03 08:05:48', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:05:48', '2026-01-03 08:06:01', 'revoked_single'),
(110, 9, '169a4582251430a690a16632777eda65f3d8b5d58943c9a65e99d53e63670630', '9cf89a23f7db4bebc8dacd55e1b293dcfc1914e62e5150bd9fe9e3408a1c721b', 'REVOKED', '2026-01-10 08:06:19', '2026-01-03 08:06:19', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:06:19', '2026-01-03 08:08:28', 'revoked_by_system'),
(111, 9, 'bc270fd4efd838ece058ddc02c352ca0ed61c8ecf4eb782f8b1f05f9d2ed7cf9', 'ed806c720cacf6833ef87af4137287169c74767b6d9fff8710ee0a1808ebc1a0', 'REVOKED', '2026-01-10 08:08:28', '2026-01-03 08:08:28', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:08:28', '2026-01-03 08:10:05', 'revoked_single'),
(112, 9, '9d1b15fb8991a7424ecc8128983cfc6484bee4d8163b8413771e18379ce82a28', '33e9747377dbc03d7114ff30823069f3cfd53e7035d0d5a5117e3b20d736b304', 'REVOKED', '2026-01-10 08:10:12', '2026-01-03 08:10:12', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:10:12', '2026-01-03 08:10:16', 'revoked_single'),
(113, 9, '8c44f41f9380ee0177b135ee6cafb51b2b4d4010014f9ebe807a9b299f356d88', '8d824faa5f379a5b6e63676d5a88ec099f807f4ad76b7287c2d5623e7415c48e', 'REVOKED', '2026-01-10 08:19:36', '2026-01-03 08:26:29', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:19:36', '2026-01-03 08:26:31', 'revoked_single'),
(114, 9, '4fa783754adfd30f2d26ca3fa16e71181a5359d00bb0fd136597618020ece78c', 'a598c7193afd4d50edcb7ae9b0307ea9d5a6885f4a33f9814ed0bb02a988f0fb', 'REVOKED', '2026-01-10 08:26:40', '2026-01-03 08:26:40', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:26:40', '2026-01-03 08:27:14', 'revoked_by_system'),
(115, 9, 'd319780f7a368c18676e33596bc5d684fdfb53602b71c07b0bf4505c8fa6364b', '897757a84355958c7d7d18dde4f853fc690f842ce8b72821dd4ad89f9b2fe68b', 'REVOKED', '2026-01-10 08:27:14', '2026-01-03 08:27:14', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:27:14', '2026-01-03 08:38:58', 'revoked_single'),
(116, 9, '858aae60f833e7e4ee89a31d7d3ad6d564056b337b67d550142401d17f91a058', 'a13e03701bb86d4c970fbac205e9e9552908fbbe7dd4cb07116bd9af7cf85309', 'REVOKED', '2026-01-10 08:39:39', '2026-01-03 08:40:28', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:39:39', '2026-01-03 08:40:31', 'revoked_single'),
(117, 9, '874ff3596c1155517ebcd209a098b07b64e0b39fb618f5e630fa0396da9fdc65', 'aa317486bee97c39b8fd2482ea73e82ab8afa1ba025f04301abd7b38ea725bd7', 'REVOKED', '2026-01-10 08:40:43', '2026-01-03 08:40:43', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:40:43', '2026-01-03 08:41:12', 'revoked_single'),
(118, 9, 'd15651d2c8b1b91e20d0c57e2dd7d54359539f0ea7a68141e4df4e0ad8d815ac', 'cf3534cf7227a38c8c63e314f5b554b55f3e70fb14d79133f851b93aa4ad6125', 'REVOKED', '2026-01-10 08:41:47', '2026-01-03 08:41:47', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:41:47', '2026-01-03 08:50:37', 'revoked_by_system'),
(119, 9, 'ef28c6d8002ac6b3092036c3fc0a6bcd80e625fdd322bb17e8cd00c2db2e4543', '8ca18fe59268bca2990e39124e823ad7c14b172f8510b5d87a9f9cd99472af28', 'REVOKED', '2026-01-10 08:50:37', '2026-01-03 08:50:37', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:50:37', '2026-01-03 08:54:22', 'revoked_single'),
(120, 9, '63a27e52399b755ab525492a1978562ae89506a39c19a51a35e2bfe4f68e89b8', '1850facd5eeb2776fd392e5c86725f154ab2b024e601eac70c3a8ae1fbff889c', 'REVOKED', '2026-01-10 08:54:35', '2026-01-03 08:54:35', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 08:54:35', '2026-01-03 09:06:18', 'revoked_by_system'),
(121, 9, '4f9080fb32d9c4cc507dd1d4fac56cbeb466bbd5d54f0383c506b473eac42440', '639bdb51319961a0c23c4ba74cbe7a11b4d6638bd050f5a3b411ee8532165569', 'REVOKED', '2026-01-10 09:06:18', '2026-01-03 09:06:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:06:18', '2026-01-03 09:06:29', 'revoked_single'),
(122, 9, '9b5b848aa1b54bc4b0e010321356b90376e4c18f4e6a2523c4e4059e7ca069dd', 'c7b9a0212050d7b0c523be41c1d009621d2041f842ec7e43cb5edc509a2a8cd8', 'REVOKED', '2026-01-10 09:06:42', '2026-01-03 09:06:42', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:06:42', '2026-01-03 09:06:52', 'revoked_single'),
(123, 9, 'f1a4c6e1d78f88f067320c1777aae98a0744b443ffa254fc5c2e4a55601e3b73', '0730ea7c4ebe22d0258214f9a620570a37667aadf858b4aacd57e6d340f7d7ae', 'REVOKED', '2026-01-10 09:07:09', '2026-01-03 09:07:09', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:07:09', '2026-01-03 09:07:25', 'revoked_single'),
(124, 9, '340d1593a3eeecaf252300c1ee59881af2cf3b5abb2ddf3f4052320bf5245e9e', 'de02cf9454fb6f58ad9e0728697b42a050cb913456b542b3a7b73305a4173db7', 'REVOKED', '2026-01-10 09:07:59', '2026-01-03 09:07:59', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:07:59', '2026-01-03 09:08:55', 'revoked_single'),
(125, 9, '189cfad89b3140157de125fc5138c7d515409724a6448a6acd525abe9c7cadc3', '9e9802821681b24bd9f105da71b8e878c308b475dc884879a9c6aed7c913cae5', 'REVOKED', '2026-01-10 09:13:35', '2026-01-03 09:13:35', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:13:35', '2026-01-03 09:13:39', 'revoked_single'),
(126, 9, 'ea33061679128c3730f044426c5cf1ff539c15e0a26f2cc6b72f17afa81524d9', 'b9113adce2cf4e7bc2d0de8d553b7391d723c5e5306d83f3c7f9d696b2dcd82d', 'REVOKED', '2026-01-10 09:17:37', '2026-01-03 09:17:37', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:17:37', '2026-01-03 09:26:07', 'revoked_by_system'),
(127, 9, '30504f314c961dd5226ba5240740ee20b2aa72ae0bead8b294e79faf816f2f9f', 'fbe3081b0cf129c2a5ce74a1f99c69e71bb2bba02d76067c13fa547d0647a499', 'REVOKED', '2026-01-10 09:26:07', '2026-01-03 09:26:07', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:26:07', '2026-01-03 09:26:10', 'revoked_single'),
(128, 9, 'bd1ab7b32c3e7afcbe5f0f1124f0a3acb1d827b208e73370b37a24bc03420566', 'c9bc18cf79ef53b9180c007ba0cf4639e32b87dcb8ec0546214de1d62a435e72', 'REVOKED', '2026-01-10 09:26:26', '2026-01-03 09:26:26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:26:26', '2026-01-03 09:27:39', 'revoked_single'),
(129, 9, 'a3b912e64fc5c59ae4098535ac8b43383a26cc724f41922664373ac51b1ae1a8', '60c3e235ee0761156f8b368de76d8f0beadab413694b7e225365e72a8140604a', 'REVOKED', '2026-01-10 09:27:49', '2026-01-03 09:27:49', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:27:49', '2026-01-03 09:27:51', 'revoked_single'),
(130, 9, 'fe3a1aa3c50072f71c3defab865a2fc6dac4563ea499e182abaf79024f51252a', 'd53b8d6bd85bc55baacbe5f1387583522006a6ef73f5757afb47acf176c63c0f', 'REVOKED', '2026-01-10 09:48:03', '2026-01-03 09:48:03', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 09:48:03', '2026-01-03 09:48:09', 'revoked_single'),
(131, 9, 'ee4b76506f8ab0e93ab98179b2f95cd4fcfa17a51a98c9c2dfd2ebe09131744a', '117183b3864664d79ff2a98d7cf2f776992ebfc3edbf3f19081e4bbc7b50f5ec', 'REVOKED', '2026-01-10 10:14:26', '2026-01-03 10:14:26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 10:14:26', '2026-01-03 10:14:29', 'revoked_single'),
(132, 9, '2684b4cc4f8119cf985f47ebe585438c3c916fbfef1d4d5e3c6e4b9ae5b47948', 'a2a96fad1c0e4db06fdfabbc86e08e25bb2bf54ac352c702377c5c513b16665f', 'REVOKED', '2026-01-10 10:16:15', '2026-01-03 10:16:15', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 10:16:15', '2026-01-03 10:16:19', 'revoked_single'),
(133, 9, '76b6152b07a1899c6e369f567f7aeee58cc652046c7023f51907a5e3e7494362', '52442320d35cdec723b355ee07ee5e7935d3ab0b0822f6c80bac99ded79fafb0', 'REVOKED', '2026-01-10 10:23:29', '2026-01-03 10:23:29', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 10:23:29', '2026-01-03 10:24:04', 'revoked_single'),
(134, 9, '4aaea83339bf2954e5bc247f4ba3438800b9653ac3e3174a9d73a0669d76b789', '62f55b27534d7a2b21505ec3a51344ed30a7d8dfc6d14ea00d17f81cdaadfd4f', 'REVOKED', '2026-01-10 10:25:54', '2026-01-03 10:25:54', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 10:25:54', '2026-01-03 10:25:57', 'revoked_single'),
(135, 9, '744f6f0c938de9c424ee0d5d9a0d1b7441fe5dcf521ab1fc732b62454460dba6', '864d6c141d67aadafa51579530859f35dac6e0f50facd8f3128a3fc9b18b7d2c', 'REVOKED', '2026-01-10 10:28:01', '2026-01-03 10:28:02', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 10:28:02', '2026-01-03 10:28:04', 'revoked_single'),
(136, 9, '1a41a350d4c371fb7b2c3406efb32b5479a92a9c28c73bbb77c7d62c0e9df91d', '425b0dee320bc602900ad6939b9ef36fb95b76129cb5b761a6fa16c73e941731', 'REVOKED', '2026-01-10 11:34:25', '2026-01-03 11:34:25', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 11:34:25', '2026-01-03 11:34:28', 'revoked_single'),
(137, 9, '073887c7c4eb3d783e18e87960a93d6cd9cd66588fd856455aa4e6860f51c52a', '839089858f55f8f831125a57612c21e6c001e24e2c054c357704a92080e4bfc2', 'REVOKED', '2026-01-10 12:27:43', '2026-01-03 12:27:43', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 12:27:43', '2026-01-03 12:27:48', 'revoked_single'),
(138, 9, 'b1900964b0721dd42c5be2016430bd17f80f2878220cdc63b3be68590ce828eb', 'e912df6f86e0e2781e9a2d67d0d630ab6fec58ce8a453973b880c3f28355dda2', 'REVOKED', '2026-01-10 20:18:42', '2026-01-03 20:18:42', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:18:42', '2026-01-03 20:22:42', 'revoked_by_system'),
(139, 9, 'fb8bcba5e9ca076f9470c6433d2d91483c1e0c8e2a3dd50053857b9cb1deaeff', 'ee33f0efa4c9243a594c395b9930b0c1f2206bdb11a133c1d8a4902edc7b7cd2', 'REVOKED', '2026-01-10 20:22:42', '2026-01-03 20:22:42', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:22:42', '2026-01-03 20:22:55', 'revoked_single'),
(140, 9, '64319b49df9bc85085d7892f43c076521fcf35034f880c81a2d285e3cd8fae64', '8594cc852809b047ce751ed557451bccc9682a94febae29c108ea1fd4ebd6498', 'REVOKED', '2026-01-10 20:23:18', '2026-01-03 20:23:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:23:18', '2026-01-03 20:24:07', 'revoked_single'),
(141, 9, '7b948db05e83bdbd645ed4265137b86c1128c94924d6cabfcdcc92b152f9b671', '051ce1eed04acdeaab0f2d1025ef3c38215ac8c7a8329d270683e781362f3f27', 'REVOKED', '2026-01-10 20:24:18', '2026-01-03 20:24:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:24:18', '2026-01-03 20:24:27', 'revoked_single'),
(142, 9, '09209df4e7b328bb58c90ca38682d7d30ae242d00398417d5c9176d4225f3c8d', '9d2e6c1a7e7db36cf8a19ef57d544caaf5f1a3aebbec27aaa68a22db208ef4c9', 'REVOKED', '2026-01-10 20:25:07', '2026-01-03 20:25:07', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:25:07', '2026-01-03 20:25:39', 'revoked_single'),
(143, 9, '94cd9632fe11b6d2c0efb7299c65e07dcff648b192f58b874b91d85eec58c1f9', 'ebb2526c4b07eee338b7d3f6622631cd48b381f7db58127894f83e190dda38db', 'REVOKED', '2026-01-10 20:28:03', '2026-01-03 20:28:03', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:28:03', '2026-01-03 20:29:43', 'revoked_by_system'),
(144, 9, '1ca2e4070d3371e8f6d3f33804af4699f1ac0f20041a2acf3d5c09cc63fba755', 'b3c6e2300ba1a16d37184c35626b32f02481f11055dcd547af37e37e4b96e0f5', 'REVOKED', '2026-01-10 20:29:43', '2026-01-03 20:46:12', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:29:43', '2026-01-03 20:46:14', 'revoked_single'),
(145, 9, '1d2d514742e8f62e52b3110307503cf4ba7fa3be40139d70ad7c92fdef582d17', '4ba52db4305e990a752cb7b0f72c267cf3d0ff865af9e3abcdbbe8aa0246dc11', 'REVOKED', '2026-01-10 20:51:56', '2026-01-03 20:51:56', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:51:56', '2026-01-03 20:54:01', 'revoked_by_system'),
(146, 9, 'aa283ce3bb864f5f2d794d2d03e42711710a9d2ab1393ab564fd44f6c7764588', '5afcd44a3f89e5858acc3f7110288c4bb7d792e7218ebc3d92bfa0d331ccd0c7', 'REVOKED', '2026-01-10 20:54:01', '2026-01-03 20:54:01', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:54:01', '2026-01-03 20:55:44', 'revoked_by_system'),
(147, 9, '75ac0c5440b0900c262b79af56ba057d7d019eba0a4e0c5dc2eab4c325150df0', '64ce0189be35e21307b6614f0ed34bed1e5fe71e59be84aaf003adf8e0d5fae0', 'REVOKED', '2026-01-10 20:55:44', '2026-01-03 20:55:44', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 20:55:44', '2026-01-03 21:10:18', 'revoked_by_system');
INSERT INTO `auth_tokens` (`id`, `user_id`, `auth_hash`, `device_id`, `status`, `expired_at`, `last_rotated_at`, `ip_last`, `user_agent`, `created_at`, `revoked_at`, `revoked_reason`) VALUES
(148, 9, '9356683bff06789e30da80c5a80e529011a5526aa62d5e8c0386f5b600d13c40', 'e899aa45a016a3ed78827ea8c15022cf192e68603e6aefcf9d53c579eb20530f', 'REVOKED', '2026-01-10 21:10:18', '2026-01-03 21:10:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 21:10:18', '2026-01-03 21:15:19', 'revoked_by_system'),
(149, 9, 'b764b8a835aac2d33e9743633b31331655a6c48008dff17a305cfc8893f7543f', 'd70502333255682235b7834999f9f7c19a78740b31ae6bd0793e7f4a4e356050', 'REVOKED', '2026-01-10 21:15:19', '2026-01-03 21:40:18', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 21:15:19', '2026-01-03 21:40:37', 'revoked_single'),
(150, 9, 'a264b3506520c078ea411b199824e7ce0baee7149b1098516663948ce17ddad9', 'bc1ed10981a9fc1615fa1ad89c2cb51043a972f8698841d9ba82f8efab80c499', 'REVOKED', '2026-01-10 21:43:14', '2026-01-03 21:43:14', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 21:43:14', '2026-01-03 21:51:26', 'revoked_by_system'),
(151, 9, '1e7ac491f34a2e7f96a302f69f341683776618b0db1d4b4104bdf987bcc1c0fc', '358ebf2adfa5e019c885d983ab307a051c5b4d910051a1263e21750f3be897e6', 'REVOKED', '2026-01-10 21:51:26', '2026-01-03 21:51:26', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 21:51:26', '2026-01-03 21:51:45', 'revoked_single'),
(152, 9, 'f5614a55136d73bf246b7bf10e35292ce06472256b6b6153d3093f84f8ab1a5f', '82b2480a908949b42d55165353631b068db6b2d5f553031d28bcf66185477428', 'REVOKED', '2026-01-10 21:52:07', '2026-01-03 21:53:32', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 21:52:07', '2026-01-03 21:53:34', 'revoked_single'),
(153, 9, 'e2e9ca7c79097b40a9a26955f31dd266a12c64080c5c1a33f39a88286a66e68f', '7c9bb65d82144ee7cb6100d38b58442127d2de98013551747c50c17f3d051df2', 'REVOKED', '2026-01-10 21:53:42', '2026-01-03 21:53:42', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 21:53:42', '2026-01-03 21:54:10', 'revoked_single'),
(154, 9, 'a23126807361b13d342a990beb7fa199c58e8703d019225172c3db371917fa38', '6d83c2002073ef766f96d359e8a5553aabfab6796bb1981d3980725f2994da19', 'REVOKED', '2026-01-10 22:55:39', '2026-01-03 22:55:39', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-03 22:55:39', '2026-01-03 22:55:42', 'revoked_single'),
(155, 9, 'ea3ed6adc1a399e6f43a1290dd4834054e2dc16223f8ad0a1a4607b372f6d48c', '2580950666be12a3876a6a6f4eb3a269c69308e48f15f8d7f8d42234df79ca81', 'REVOKED', '2026-01-11 01:13:30', '2026-01-04 01:13:30', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-04 01:13:30', '2026-01-04 01:13:33', 'revoked_single'),
(156, 9, '80fcda891090686d913c67ac3d26224857c496c7f43da5b9c3352ca9dc6f70cf', '6e983b8627c5728e5016881e1a94af1e407e379d45c4c23213580419f29bdf95', 'REVOKED', '2026-01-11 01:53:27', '2026-01-04 01:53:27', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-04 01:53:27', '2026-01-04 01:53:30', 'revoked_single'),
(157, 9, '0c93f37d04ed217417381f1b1e1d2b58bf255f57bd8824e2a61e5ebe85d7463b', 'd2f29d81988af3b4c1d3b8932fd044ccf4e18a50bf88349fd3a85603b0707e2a', 'REVOKED', '2026-01-11 02:00:33', '2026-01-04 02:00:33', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-04 02:00:33', '2026-01-04 02:00:37', 'revoked_single'),
(158, 9, '577265f6afc7382796f7f8b42779e4e214750723bf43ac1436652083fba167b4', 'e69b22b38fe6d90b0014e0f723dcc9d43eb94145e5cd3008ed2a68636eb60046', 'REVOKED', '2026-01-11 07:54:40', '2026-01-04 07:54:40', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 07:54:40', '2026-01-04 07:54:49', 'revoked_single'),
(159, 9, 'b5d0f8c772db97fbb4242225c01f37d4c5387c5d77556b4f1b37090a823b2fbe', '6b141dd2b23eed4b449f6aadc4eb6dce28b3cdf662d5db9ea55f4c681c1b936b', 'REVOKED', '2026-01-11 09:01:43', '2026-01-04 09:02:35', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:01:43', '2026-01-04 09:02:38', 'revoked_single'),
(160, 9, 'b051e4d9753cef8310bc770ce4938528909a3f71ef9c1ae4b8696aa9f26d82fe', '3d69d13b94f78ff52f36c981b4a498b1b6b547a3ca8ba42e77fc2c60f261db14', 'REVOKED', '2026-01-11 09:03:22', '2026-01-04 09:03:22', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:03:22', '2026-01-04 09:04:05', 'revoked_single'),
(161, 9, '8ca10f88222346ddc472141ea7c78880f8c8716905c6c3d443bbf9de20b1468e', 'c66b62d624bfbf5cb75b5077361b7afbeb00469279f2154a7cf172a8e5aa461a', 'REVOKED', '2026-01-11 09:18:16', '2026-01-04 09:23:12', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:18:16', '2026-01-04 09:23:12', 'revoked_single'),
(162, 9, '671e0c58cc22c27261884f7b866d23467339d81f5940f72f161126bde7bcfd8a', '4d3b5d5efae500adea24398400b6736b96e9271f82802943ecc223abaf603aa1', 'REVOKED', '2026-01-11 09:23:41', '2026-01-04 09:26:53', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:23:41', '2026-01-04 09:26:57', 'revoked_single'),
(163, 9, '00cabc28c9443f06be566ab16e46206c5ed565e3caf27eb272290b8218652c48', 'b07e9914d99e1c3ccfd4f5677b69c4fad670af82625b617663b2106aaabb093e', 'REVOKED', '2026-01-11 09:27:07', '2026-01-04 09:27:07', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:27:07', '2026-01-04 09:29:29', 'revoked_by_system'),
(164, 9, '5199ec0cb6378ae8b78d510e0e0e4b4d352bd2388a06f5718ba92e027d4fd852', 'b07e9914d99e1c3ccfd4f5677b69c4fad670af82625b617663b2106aaabb093e', 'REVOKED', '2026-01-11 09:29:29', '2026-01-04 09:29:29', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:29:29', '2026-01-04 09:29:54', 'revoked_by_system'),
(165, 9, 'ba0dadd617aa1b4c21dd0ae892da76bb4c1485f6752c060c8ede9b130221aa30', 'f472acad07bf32e49c72f7d201a33b9557186551e37e7a6163060b510cfa2d1b', 'REVOKED', '2026-01-11 09:29:54', '2026-01-04 15:35:09', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 09:29:54', '2026-01-04 15:35:29', 'revoked_by_system'),
(166, 9, 'cc810d87c287f2e51a9c766bb0f0f14d974277194765a6353bf2a47b58676939', 'f472acad07bf32e49c72f7d201a33b9557186551e37e7a6163060b510cfa2d1b', 'REVOKED', '2026-01-11 15:35:29', '2026-01-04 15:35:29', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 15:35:29', '2026-01-04 15:36:31', 'revoked_by_system'),
(167, 9, 'c176d9302454b64afb04ab0874a8311b302f91ac62755c79d796f2e5cc23bb6b', 'de011f1f0f2818600a5994e6e80fc1180c52104b4a0a009efeaeee3e6cb72368', 'REVOKED', '2026-01-11 15:36:31', '2026-01-04 15:36:31', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 15:36:31', '2026-01-04 15:36:35', 'revoked_single'),
(168, 9, 'd32c0a4d319a05054d16a5d10eaf2f238b0534fcb947a4dd570ed92b99b71b06', '310e2e407141a35a12f5dd85132547daaa723bf3dc7840f010bfc473bb3a3230', 'REVOKED', '2026-01-11 15:36:59', '2026-01-04 15:36:59', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 15:36:59', '2026-01-04 15:37:02', 'revoked_single'),
(169, 9, '6692ad082a7d0adfb53173e7af500506a0cac3faa60cab11d6e00aec13737e24', '7569c475028d76c34f96f864346aa6b7488f2b9ed02e5436f0a74ec11a7cf5f2', 'REVOKED', '2026-01-11 17:11:02', '2026-01-04 17:11:02', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 17:11:02', '2026-01-04 17:11:05', 'revoked_single'),
(170, 9, '0888d01c65879e942be6248e9ebe7de3b72807e516d71c6d39838e15f1a0325b', 'b98ae5aa7c239b648bb1f028bb70eff50eb24c3ed8845cc1b69721499ff3274d', 'REVOKED', '2026-01-11 17:54:51', '2026-01-04 17:54:51', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 17:54:51', '2026-01-04 17:54:54', 'revoked_single'),
(171, 9, 'eb854b863e0872b8ad7ccbd04e1c20f1a6e1921691f6794b6cf8b7d3d672a6f5', '32044dc61dfe0a07063602b733e43d6880334af6b050e1004292c7af008c195b', 'REVOKED', '2026-01-11 17:55:03', '2026-01-04 17:55:03', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 17:55:03', '2026-01-04 17:55:46', 'revoked_single'),
(172, 9, '385993dcea007bc49673d25067c9bfc6c3101ac4d69d7593b3cdf7e0a5d2a581', '85e71897b8f15755c021f020050d005ee5d2427cee8b7b6ae03c437f450f28c4', 'REVOKED', '2026-01-11 17:56:00', '2026-01-04 18:30:12', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-04 17:56:00', '2026-01-05 11:29:49', 'revoked_by_system'),
(173, 9, '14f4ab05d8c9cc3ff7e75f11ec47fe6dd1754d7c49bedcd02bda5e00c73f0c3e', '9059619f4b8e31ea55efe7ef3087d6d15db424a6981605119f30387042206997', 'REVOKED', '2026-01-12 11:29:49', '2026-01-05 11:29:49', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-05 11:29:49', '2026-01-05 11:29:53', 'revoked_single'),
(175, 9, 'a16b175b87cf9ce2261265a843fa713fce9c814094f0f42b154e74a5ec1383ee', '8ae0e9baf53c5dd5c961fe675f48ce1c1283311392f3bba68d94adc4048593c0', 'REVOKED', '2026-01-13 09:21:07', '2026-01-06 09:21:07', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-06 09:21:07', '2026-01-06 09:22:30', 'revoked_by_system'),
(177, 9, 'f539ab3e1fd7b243c538a5f2a9711e75d9d9e027eea1870a61b74d4a8401cf3f', 'fe739d4dd07e36b65c1f53b65e290b83746c779c96f462a33bd73019e5348d0f', 'REVOKED', '2026-01-13 09:22:30', '2026-01-06 10:29:52', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-06 09:22:30', '2026-01-07 11:50:54', 'revoked_by_system'),
(178, 14, '0168136fb071d99eca3036ef08f672a76748853b1ba3073a1da65f2e46ff70d0', '781d0518c8ae85faf05b9b6b96e48b77682b8f95f416301a8c78884a86fa1d6a', 'ACTIVE', '2026-01-13 10:31:47', '2026-01-06 10:31:47', '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0', '2026-01-06 10:31:47', NULL, NULL),
(179, 9, 'b1d05bb021f22b0af47c1a2cff2a2930ed31cbb5c9fd6abb09de855945acca6c', '18b3e5e697567eb766a9b92d0b1a0388b99ed1173c0a3a9f335189652dfa4e08', 'REVOKED', '2026-01-14 11:50:54', '2026-01-07 11:50:54', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-07 11:50:54', '2026-01-07 12:00:36', 'revoked_by_system'),
(180, 9, '816dafcef9e0f003648a16db2cf4cc6b42cfb9aa53565f55a7278a2a1f1c4b6d', 'b196acce12d9a4b1925852664fb22e6ea820dfabefdd6d3a0bffd2832db38886', 'REVOKED', '2026-01-14 12:00:36', '2026-01-07 12:00:36', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-07 12:00:36', '2026-01-07 12:36:12', 'revoked_by_system'),
(181, 9, 'd9f601264e52b93f49225c35f61467badaa70db14e76a2e12a68968c089f91d8', '707cf95e7745667751b1918fb45d7e3032e9377cc5d06c6dae8d3ff7c2805afe', 'REVOKED', '2026-01-14 12:36:12', '2026-01-07 12:36:12', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-07 12:36:12', '2026-01-08 09:26:14', 'revoked_by_system'),
(182, 9, '20beaf000901796552325d6bc209c817d5888194236441f00b61d315db8ce185', '58d9a837b7479a85cb940abf7a4c83047a3714f9bea3453f969bbcc39fbe79d5', 'REVOKED', '2026-01-15 09:26:14', '2026-01-08 09:26:14', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-08 09:26:14', '2026-01-08 11:31:22', 'revoked_by_system'),
(183, 9, '58e5249649dc98638e6b72574b639d88f91e8ed85f96a9f663276dc18bb25601', 'd9d394ea4ea4999cff3d2f1fa81677160abeaa4046ad0e8fdd08fab6796706e6', 'REVOKED', '2026-01-15 11:31:22', '2026-01-08 12:55:40', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-08 11:31:22', '2026-01-08 13:46:25', 'revoked_by_system'),
(184, 9, '3ff963ba9756dd04877acea50970d96dd78412424f8c0d2621179b4e50e228dd', '6ed8f03ca86d79ec042a1f89fe76f357768f361d072e3789255d3f5517de7f60', 'ACTIVE', '2026-01-15 13:46:25', '2026-01-08 13:46:25', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:146.0) Gecko/20100101 Firefox/146.0', '2026-01-08 13:46:25', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bookings`
--

CREATE TABLE `bookings` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `table_id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `start_at` datetime NOT NULL,
  `end_at` datetime NOT NULL,
  `status` enum('pending','confirmed','seated','no_show','canceled','expired') NOT NULL DEFAULT 'pending',
  `deposit_amount` int(11) NOT NULL DEFAULT 0,
  `seat_fee_snapshot` int(11) NOT NULL DEFAULT 0,
  `late_cancel_threshold_pct` int(11) NOT NULL DEFAULT 50,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `booking_items`
--

CREATE TABLE `booking_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `booking_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `qty` int(11) NOT NULL DEFAULT 1,
  `unit_price_snapshot` int(11) NOT NULL,
  `payment_status` enum('unpaid','paid','canceled') NOT NULL DEFAULT 'unpaid',
  `note` varchar(160) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `carts`
--

CREATE TABLE `carts` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `carts`
--

INSERT INTO `carts` (`id`, `store_id`, `user_id`, `created_at`, `updated_at`) VALUES
(2, 3, 9, '2025-12-30 12:58:36', '2025-12-30 12:58:36'),
(3, 1, 9, '2025-12-30 13:21:14', '2025-12-30 13:21:14'),
(4, 2, 9, '2025-12-30 14:24:40', '2025-12-30 14:24:40');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_items`
--

CREATE TABLE `cart_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `cart_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `qty` int(11) NOT NULL DEFAULT 1,
  `unit_price_snapshot` int(11) NOT NULL,
  `options_price_snapshot` int(11) NOT NULL DEFAULT 0,
  `note` varchar(160) DEFAULT NULL,
  `item_hash` char(64) NOT NULL,
  `item_name_snapshot` varchar(120) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cart_items`
--

INSERT INTO `cart_items` (`id`, `cart_id`, `menu_item_id`, `qty`, `unit_price_snapshot`, `options_price_snapshot`, `note`, `item_hash`, `item_name_snapshot`, `created_at`, `updated_at`) VALUES
(44, 3, 29, 51, 45000, 6000, NULL, 'e773eedd6045bf1da0d21b019d6c2fa95282a0cb49a9557063e73e5b8ac42896', '{\"vi\": \"Bạc Xỉu Caramel Muối\", \"en\": \"Bac Xiu Salted Caramel\"}', '2025-12-30 14:33:27', '2026-01-08 09:37:31'),
(45, 3, 28, 1, 45000, 6000, 'Bé yiuo', '9ce27ed78216b651d324cbf1f34b7cdaa187b91c1756605ac513ab7d69274bd2', '{\"vi\": \"Bạc Xỉu Foam Dừa\", \"en\": \"Bac Xiu Coconut Foam\"}', '2025-12-30 14:33:27', '2026-01-08 09:37:31'),
(46, 3, 10, 1, 49000, 6000, NULL, '5c3efb5a95addfae20ae45e36932b7e803f590b52b08600dba74b1343c660945', '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', '2025-12-30 14:33:27', '2026-01-08 09:37:31'),
(47, 4, 10, 1, 49000, 10000, NULL, '6c065836cceed4cea742fe91354ee4258a5eb502c5184ca7bde2feb0a2576d7f', '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', '2025-12-30 14:38:33', '2026-01-08 09:37:31');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `cart_item_options`
--

CREATE TABLE `cart_item_options` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `cart_item_id` bigint(20) UNSIGNED NOT NULL,
  `option_value_id` bigint(20) UNSIGNED NOT NULL,
  `option_group_name_snapshot` varchar(80) NOT NULL,
  `option_value_name_snapshot` varchar(80) NOT NULL,
  `price_delta_snapshot` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `cart_item_options`
--

INSERT INTO `cart_item_options` (`id`, `cart_item_id`, `option_value_id`, `option_group_name_snapshot`, `option_value_name_snapshot`, `price_delta_snapshot`) VALUES
(39, 44, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(40, 45, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(41, 46, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(42, 47, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`name`)),
  `slug` varchar(100) DEFAULT NULL,
  `icon_url` varchar(255) DEFAULT NULL,
  `order_index` int(11) NOT NULL DEFAULT 1,
  `is_active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`id`, `name`, `slug`, `icon_url`, `order_index`, `is_active`) VALUES
(1, '{\"vi\": \"Americano\", \"en\": \"Americano\"}', 'americano', 'assets/client/img/categories/americano.png', 1, 1),
(2, '{\"vi\": \"Trà trái cây\", \"en\": \"Fruit tea\"}', 'fruit-tea', 'assets/client/img/categories/fruit-tea.png', 8, 1),
(3, '{\"vi\": \"Trà sữa\", \"en\": \"Milk tea\"}', 'milk-tea', 'assets/client/img/categories/milk-tea.png', 7, 1),
(4, '{\"vi\": \"Đá xay\", \"en\": \"Ice blended\"}', 'ice-blended', 'assets/client/img/categories/ice-blended.png', 4, 1),
(7, '{\"vi\": \"Bánh ngọt\", \"en\": \"Cake\"}', 'cake', 'assets/client/img/categories/cake.png', 12, 1),
(9, '{\"vi\": \"Espresso\", \"en\": \"Espresso\"}', 'espresso', 'assets/client/img/categories/espresso.png', 2, 1),
(10, '{\"vi\": \"Latte\", \"en\": \"Latte\"}', 'latte', 'assets/client/img/categories/latte.png', 5, 1),
(11, '{\"vi\": \"Cà phê phin\", \"en\": \"Phin coffee\"}', 'phin-coffee', 'assets/client/img/categories/coffee.png', 3, 1),
(12, '{\"vi\": \"Cold brew\", \"en\": \"Cold brew\"}', 'cold-brew', 'assets/client/img/categories/cold-brew.png', 4, 1),
(13, '{\"vi\": \"Matcha\", \"en\": \"Matcha\"}', 'matcha', 'assets/client/img/categories/matcha.png', 6, 1),
(15, '{\"vi\": \"Topping\", \"en\": \"Topping\"}', 'topping', 'assets/client/img/categories/topping.png', 11, 1),
(16, '{\"vi\": \"Bánh mặn\", \"en\": \"Savory pastry\"}', 'savory-pastry', 'assets/client/img/categories/banh-man.png', 13, 1),
(17, '{\"vi\": \"Món nóng\", \"en\": \"Hot drink\"}', 'hot-drink', 'assets/client/img/categories/hot-drink.png', 10, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `invoices`
--

CREATE TABLE `invoices` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `order_id` bigint(20) UNSIGNED NOT NULL,
  `invoice_number` varchar(64) NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `issued_at` datetime NOT NULL,
  `subtotal_amount` int(11) NOT NULL DEFAULT 0,
  `discount_amount` int(11) NOT NULL DEFAULT 0,
  `tax_amount` int(11) NOT NULL DEFAULT 0,
  `total_amount` int(11) NOT NULL DEFAULT 0,
  `status` enum('issued','void') NOT NULL DEFAULT 'issued',
  `buyer_name` varchar(120) DEFAULT NULL,
  `buyer_tax_id` varchar(32) DEFAULT NULL,
  `buyer_address` varchar(255) DEFAULT NULL,
  `buyer_email` varchar(190) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `invoices`
--

INSERT INTO `invoices` (`id`, `order_id`, `invoice_number`, `store_id`, `user_id`, `issued_at`, `subtotal_amount`, `discount_amount`, `tax_amount`, `total_amount`, `status`, `buyer_name`, `buyer_tax_id`, `buyer_address`, `buyer_email`, `created_at`) VALUES
(1, 2, 'INV-3-20251230143959', 3, 9, '2025-12-30 14:39:59', 59000, 0, 0, 59000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 14:39:59'),
(2, 3, 'INV-3-20251230144056', 3, 9, '2025-12-30 14:40:56', 39000, 0, 0, 39000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 14:40:56'),
(3, 4, 'INV-3-20251230145203', 3, 9, '2025-12-30 14:52:03', 39000, 0, 0, 39000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 14:52:03'),
(4, 5, 'INV-3-20251230150237', 3, 9, '2025-12-30 15:02:37', 1595000, 0, 0, 1595000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 15:02:37'),
(5, 6, 'INV-3-20251230150320', 3, 9, '2025-12-30 15:03:20', 102000, 0, 0, 102000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 15:03:20'),
(6, 7, 'INV-3-20251230150738', 3, 9, '2025-12-30 15:07:38', 55000, 0, 0, 55000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 15:07:38'),
(7, 8, 'INV-3-20251230215438', 3, 9, '2025-12-30 21:54:39', 236000, 0, 0, 236000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 21:54:39'),
(8, 9, 'INV-3-20251230215453', 3, 9, '2025-12-30 21:54:53', 118000, 0, 0, 118000, 'issued', 'trungvan.me', NULL, NULL, 'trungvan.me@gmail.com', '2025-12-30 21:54:53');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `item_option_groups`
--

CREATE TABLE `item_option_groups` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `option_group_id` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `item_option_groups`
--

INSERT INTO `item_option_groups` (`id`, `menu_item_id`, `option_group_id`) VALUES
(23, 9, 5),
(59, 9, 6),
(24, 10, 5),
(26, 11, 5),
(27, 12, 5),
(28, 13, 5),
(29, 14, 5),
(30, 15, 5),
(31, 16, 5),
(32, 17, 5),
(33, 18, 5),
(34, 19, 5),
(35, 20, 5),
(36, 21, 5),
(37, 22, 5),
(38, 23, 5),
(39, 24, 5),
(40, 25, 5),
(41, 26, 5),
(42, 27, 5),
(43, 28, 5),
(44, 29, 5),
(45, 30, 5),
(46, 31, 5),
(47, 32, 5),
(48, 33, 5),
(49, 34, 5),
(50, 35, 5),
(51, 36, 5),
(52, 37, 5),
(53, 38, 5),
(57, 40, 5),
(58, 41, 5),
(54, 65, 5),
(56, 67, 5),
(55, 68, 5);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loyalty_accounts`
--

CREATE TABLE `loyalty_accounts` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `points_balance` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loyalty_transactions`
--

CREATE TABLE `loyalty_transactions` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED DEFAULT NULL,
  `type` enum('earn','redeem','expire','adjust') NOT NULL,
  `points` int(11) NOT NULL,
  `payment_id` bigint(20) UNSIGNED DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `reason` varchar(160) DEFAULT NULL,
  `occurred_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `menu_items`
--

CREATE TABLE `menu_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `category_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(120) NOT NULL,
  `slug` varchar(120) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `base_price` int(11) NOT NULL,
  `unit` varchar(16) NOT NULL DEFAULT 'ly',
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `menu_items`
--

INSERT INTO `menu_items` (`id`, `category_id`, `name`, `slug`, `image_url`, `description`, `base_price`, `unit`, `is_active`, `created_at`) VALUES
(9, 1, '{\"vi\": \"A-Mê Classic\", \"en\": \"A-Me Classic\"}', 'a-me-classic', 'assets/client/img/product/a-me-classic.png', '{\"vi\": \"Thức uống Americano nguyên bản từ 100% Arabica, tươi tỉnh tức thì. Uống là mê!\", \"en\": \"Original Americano from 100% Arabica beans, instantly refreshing. Drink and fall in love!\"}', 39000, 'ly', 1, '2025-12-27 07:48:32'),
(10, 1, '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', 'a-me-dao', 'assets/client/img/product/a-me-dao.png', '{\"vi\": \"Mê ly với Americano từ 100% Arabica kết hợp cùng Đào ngọt thanh, dậy vị tươi mát.\", \"en\": \"Fall in love with Americano from 100% Arabica combined with sweet peach, bringing a refreshing taste.\"}', 49000, 'ly', 1, '2025-12-27 07:54:12'),
(11, 1, '{\"vi\": \"A-Mê Mơ\", \"en\": \"A-Me Apricot\"}', 'a-me-mo', 'assets/client/img/product/a-me-mo.png', '{\"vi\": \"Mê say với Americano từ 100% Arabica kết hợp cùng Mơ chua ngọt, tươi mới mỗi ngày. *Khuấy đều để thưởng thức trọn vị\", \"en\": \"Fall in love with Americano from 100% Arabica combined with sweet-tart apricot, fresh every day. *Stir well to enjoy the full flavor.\"}', 49000, 'ly', 1, '2025-12-27 07:56:30'),
(12, 1, '{\"vi\": \"A-Mê Yuzu\", \"en\": \"A-Me Yuzu\"}', 'a-me-yuzu', 'assets/client/img/product/a-me-yuzu.png', '{\"vi\": \"Mê tít với Americano từ 100% Arabica kết hợp cùng Thanh Yên chua dịu, sảng khoái tức thì. *Khuấy đều để thưởng thức trọn vị\", \"en\": \"Fall in love with Americano from 100% Arabica combined with gently tart yuzu, instantly refreshing. *Stir well to enjoy the full flavor.\"}', 49000, 'ly', 1, '2025-12-27 07:56:28'),
(13, 1, '{\"vi\": \"Americano Nóng\", \"en\": \"Hot Americano\"}', 'americano-nong', 'assets/client/img/product/americano-nong.png', '{\"vi\": \"Americano được pha chế bằng cách pha thêm nước với tỷ lệ nhất định vào tách cà phê Espresso, từ đó mang lại hương vị nhẹ nhàng và giữ trọn được mùi hương cà phê đặc trưng.\", \"en\": \"Americano is made by adding water in a certain ratio to a shot of espresso, delivering a lighter taste while preserving the coffee aroma.\"}', 45000, 'ly', 1, '2025-12-27 07:57:12'),
(14, 9, '{\"vi\": \"Espresso Nóng\", \"en\": \"Hot Espresso\"}', 'espresso-nong', 'assets/client/img/product/espresso-nong.png', '{\"vi\": \"Một tách Espresso nguyên bản được bắt đầu bởi những hạt Arabica chất lượng, phối trộn với tỉ lệ cân đối hạt Robusta, cho ra vị ngọt caramel, vị chua dịu và sánh đặc.\", \"en\": \"A classic espresso starts with quality Arabica beans, blended with balanced Robusta for caramel sweetness, gentle acidity, and a rich body.\"}', 45000, 'ly', 1, '2025-12-27 07:58:51'),
(15, 9, '{\"vi\": \"Espresso Đá\", \"en\": \"Iced Espresso\"}', 'espresso-da', 'assets/client/img/product/espresso-da.png', '{\"vi\": \"Một tách Espresso nguyên bản được bắt đầu bởi những hạt Arabica chất lượng, phối trộn với tỉ lệ cân đối hạt Robusta, cho ra vị ngọt caramel, vị chua dịu và sánh đặc.\", \"en\": \"A classic espresso starts with quality Arabica beans, blended with balanced Robusta for caramel sweetness, gentle acidity, and a rich body.\"}', 49000, 'ly', 1, '2025-12-27 07:59:44'),
(16, 9, '{\"vi\": \"Latte Nóng\", \"en\": \"Hot Latte\"}', 'latte-nong', 'assets/client/img/product/latte-nong.png', '{\"vi\": \"Một sự kết hợp tinh tế giữa vị đắng cà phê Espresso nguyên chất hòa quyện cùng vị sữa nóng ngọt ngào, bên trên là một lớp kem mỏng nhẹ tạo nên một tách cà phê hoàn hảo về hương vị lẫn nhãn quan.\", \"en\": \"A delicate blend of pure espresso bitterness and sweet warm milk, topped with a light foam to create a cup that is perfect in flavor and appearance.\"}', 59000, 'ly', 1, '2025-12-27 08:00:42'),
(17, 9, '{\"vi\": \"Cappuccino Đá\", \"en\": \"Iced Cappuccino\"}', 'cappuccino-da', 'assets/client/img/product/cappucino-da.png', '{\"vi\": \"Capuchino là thức uống hòa quyện giữa hương thơm của sữa\", \"en\": \"Cappuccino is a drink that blends the aroma of milk.\"}', 55000, 'ly', 1, '2025-12-27 08:01:42'),
(18, 9, '{\"vi\": \"Cappuccino Nóng\", \"en\": \"Hot Cappuccino\"}', 'cappuccino-nong', 'assets/client/img/product/cappucino-nong.png', '{\"vi\": \"Capuchino là thức uống hòa quyện giữa hương thơm của sữa\", \"en\": \"Cappuccino is a drink that blends the aroma of milk.\"}', 55000, 'ly', 1, '2025-12-27 08:02:20'),
(19, 9, '{\"vi\": \"Caramel Macchiato Đá\", \"en\": \"Iced Caramel Macchiato\"}', 'caramel-macchiato-da', 'assets/client/img/product/caramel-macchiato-da.png', '{\"vi\": \"Khuấy đều trước khi sử dụng Caramel Macchiato sẽ mang đến một sự ngạc nhiên thú vị khi vị thơm béo của bọt sữa\", \"en\": \"Stir well before drinking; Caramel Macchiato brings a pleasant surprise with the creamy aroma of milk foam.\"}', 65000, 'ly', 1, '2025-12-27 08:03:34'),
(20, 9, '{\"vi\": \"Caramel Macchiato Nóng\", \"en\": \"Hot Caramel Macchiato\"}', 'caramel-macchiato-nong', 'assets/client/img/product/caramel-macchiato-nong.png', '{\"vi\": \"Caramel Macchiato sẽ mang đến một sự ngạc nhiên thú vị khi vị thơm béo của bọt sữa\", \"en\": \"Caramel Macchiato brings a pleasant surprise with the creamy aroma of milk foam.\"}', 69000, 'ly', 1, '2025-12-27 08:04:31'),
(21, 10, '{\"vi\": \"Latte Hạnh Nhân\", \"en\": \"Almond Latte\"}', 'latte-hanh-nhan', 'assets/client/img/product/latte-hanh-nhan-da.png', '{\"vi\": \"Latte Hạnh Nhân là sự kết hợp giữa Espresso đậm đà\", \"en\": \"Almond Latte is a blend of rich espresso.\"}', 59000, 'ly', 1, '2025-12-27 08:07:32'),
(22, 10, '{\"vi\": \"Latte Caramel Cold Foam\", \"en\": \"Latte Caramel Cold Foam\"}', 'latte-caramel-cold-foam', 'assets/client/img/product/latte_caremel_cold_foam.png', NULL, 59000, 'ly', 1, '2025-12-27 08:07:33'),
(23, 10, '{\"vi\": \"Latte Classic\", \"en\": \"Classic Latte\"}', 'latte-classic', 'assets/client/img/product/latte-classic.png', '{\"vi\": \"Latte Đá là sự kết hợp hoàn hảo giữa espresso đậm đà và sữa tươi béo ngậy\", \"en\": \"Iced latte is the perfect blend of bold espresso and rich fresh milk.\"}', 55000, 'ly', 1, '2025-12-27 08:08:48'),
(24, 10, '{\"vi\": \"Latte Bạc Xỉu\", \"en\": \"Bac Xiu Latte\"}', 'latte-bac-xiu', 'assets/client/img/product/latte-bac-xiu.png', '{\"vi\": \"Latte Bạc Xỉu là sự kết hợp giữa Espresso\", \"en\": \"Bac Xiu Latte is a blend of espresso.\"}', 49000, 'ly', 1, '2025-12-27 08:09:43'),
(25, 10, '{\"vi\": \"Latte Hazelnut\", \"en\": \"Hazelnut Latte\"}', 'latte-hazelnut', 'assets/client/img/product/halzenut-latte.png', '{\"vi\": \"Latte Hazelnut là sự kết hợp giữa Espresso đậm đà\", \"en\": \"Hazelnut Latte is a blend of rich espresso.\"}', 59000, 'ly', 1, '2025-12-27 08:10:18'),
(26, 12, '{\"vi\": \"Cold Brew Truyền Thống\", \"en\": \"Traditional Cold Brew\"}', 'cold-brew-truyen-thong', 'assets/client/img/product/cold_brew_truyen_thong.png', '{\"vi\": \"Tại Zero Star Cafe, Cold Brew được ủ và phục vụ mỗi ngày từ 100% hạt Arabica Cầu Đất với hương gỗ thông, hạt dẻ, nốt sô-cô-la đặc trưng, thoang thoảng hương khói nhẹ giúp Cold Brew giữ nguyên vị tươi mới.\", \"en\": \"At Zero Star Cafe, cold brew is steeped and served daily from 100% Cau Dat Arabica beans, with notes of pine, chestnut, and chocolate, and a light smoky aroma that keeps it fresh.\"}', 45000, 'ly', 1, '2025-12-27 08:12:40'),
(27, 12, '{\"vi\": \"Cold Brew Kim Quất\", \"en\": \"Kumquat Cold Brew\"}', 'cold-brew-kim-quat', 'assets/client/img/product/cold_brew_kim_quat.png', '{\"vi\": \"Vị chua ngọt của Kim Quất làm dậy lên hương vị trái cây tự nhiên vốn sẵn có trong hạt cà phê Arabica Cầu Đất\", \"en\": \"The sweet-sour taste of kumquat lifts the natural fruit notes in Cau Dat Arabica coffee beans.\"}', 49000, 'ly', 1, '2025-12-27 08:13:28'),
(28, 11, '{\"vi\": \"Bạc Xỉu Foam Dừa\", \"en\": \"Bac Xiu Coconut Foam\"}', 'bac-xiu-foam-dua', 'assets/client/img/product/bac_xiu_foam_dua.png', NULL, 45000, 'ly', 1, '2025-12-27 08:14:47'),
(29, 11, '{\"vi\": \"Bạc Xỉu Caramel Muối\", \"en\": \"Bac Xiu Salted Caramel\"}', 'bac-xiu-caramel-muoi', 'assets/client/img/product/bac_xiu_caramel_muoi.png', NULL, 45000, 'ly', 1, '2025-12-27 08:15:24'),
(30, 11, '{\"vi\": \"Bạc Xỉu\", \"en\": \"Bac Xiu\"}', 'bac-xiu', 'assets/client/img/product/bac_xiu_truyen_thong.png', '{\"vi\": \"Bạc xỉu chính là \\\"Ly sữa trắng kèm một chút cà phê\\\". Thức uống này rất phù hợp những ai vừa muốn trải nghiệm chút vị đắng của cà phê vừa muốn thưởng thức vị ngọt béo ngậy từ sữa.\", \"en\": \"Bac xiu is \\\"a glass of white milk with a little coffee\\\". This drink suits those who want a hint of coffee bitterness while enjoying the rich sweetness of milk.\"}', 39000, 'ly', 1, '2025-12-27 08:16:10'),
(31, 11, '{\"vi\": \"Bạc Xỉu Nóng\", \"en\": \"Hot Bac Xiu\"}', 'bac-xiu-nong', 'assets/client/img/product/bac_xiu_truyen_thong_nong.png', '{\"vi\": \"Bạc xỉu chính là \\\"Ly sữa trắng kèm một chút cà phê\\\". Thức uống này rất phù hợp những ai vừa muốn trải nghiệm chút vị đắng của cà phê vừa muốn thưởng thức vị ngọt béo ngậy từ sữa.\", \"en\": \"Bac xiu is \\\"a glass of white milk with a little coffee\\\". This drink suits those who want a hint of coffee bitterness while enjoying the rich sweetness of milk.\"}', 39000, 'ly', 1, '2025-12-27 08:16:52'),
(32, 11, '{\"vi\": \"Cà Phê Đen Nóng\", \"en\": \"Hot Black Coffee\"}', 'ca-phe-den-nong', 'assets/client/img/product/ca_phe_phin_den_nong.png', '{\"vi\": \"Không ngọt ngào như Bạc sỉu hay Cà phê sữa\", \"en\": \"Not as sweet as bac xiu or milk coffee.\"}', 39000, 'ly', 1, '2025-12-27 08:17:25'),
(33, 11, '{\"vi\": \"Cà Phê Sữa Nóng\", \"en\": \"Hot Milk Coffee\"}', 'ca-phe-sua-nong', 'assets/client/img/product/ca_phe_phin_nau_nong.png', '{\"vi\": \"Cà phê được pha phin truyền thống kết hợp với sữa đặc tạo nên hương vị đậm đà\", \"en\": \"Traditional phin-brewed coffee combined with condensed milk creates a bold flavor.\"}', 39000, 'ly', 1, '2025-12-27 08:18:03'),
(34, 11, '{\"vi\": \"Cà Phê Đen Đá\", \"en\": \"Iced Black Coffee\"}', 'ca-phe-den-da', 'assets/client/img/product/ca_phe_phin_den_da.png', '{\"vi\": \"Không ngọt ngào như Bạc sỉu hay Cà phê sữa\", \"en\": \"Not as sweet as bac xiu or milk coffee.\"}', 39000, 'ly', 1, '2025-12-27 08:18:41'),
(35, 11, '{\"vi\": \"Cà Phê Sữa Đá\", \"en\": \"Iced Milk Coffee\"}', 'ca-phe-sua-da', 'assets/client/img/product/ca_phe_phin_nau_da.png', '{\"vi\": \"Cà phê Đắk Lắk nguyên chất được pha phin truyền thống kết hợp với sữa đặc tạo nên hương vị đậm đà\", \"en\": \"Pure Dak Lak coffee brewed with a traditional phin and condensed milk creates a bold flavor.\"}', 39000, 'ly', 1, '2025-12-27 08:19:09'),
(36, 13, '{\"vi\": \"Matcha Latte Tây Bắc\", \"en\": \"Northwest Matcha Latte\"}', 'matcha-latte-tay-bac', 'assets/client/img/product/matcha_latte_tay_bac_da.png', '{\"vi\": \"Best seller của Nhà - rất phù hợp cho ai muốn nhập môn matcha. *Khuấy đều để thưởng trọn hương vị.\", \"en\": \"House bestseller - perfect for matcha beginners. *Stir well to enjoy the full flavor.\"}', 45000, 'ly', 1, '2025-12-27 08:20:46'),
(37, 13, '{\"vi\": \"Matcha Latte Tây Bắc (Nóng)\", \"en\": \"Hot Northwest Matcha Latte\"}', 'matcha-latte-tay-bac-nong', 'assets/client/img/product/matcha_latte_tay_bac_nong.png', '{\"vi\": \"Trà Xanh Latte (Nóng) là phiên bản rõ vị trà nhất. Nhấp một ngụm\", \"en\": \"Hot green tea latte is the most tea-forward version. Take a sip.\"}', 49000, 'ly', 1, '2025-12-27 08:21:38'),
(38, 13, '{\"vi\": \"Matcha Tây Bắc Trân Châu Hoàng Kim\", \"en\": \"Northwest Matcha with Golden Boba\"}', 'matcha-tay-bac-tran-chau-hoang-kim', 'assets/client/img/product/matcha-dao-copy-master.png', '{\"vi\": \"Matcha kết hợp đường đen Okinawa\", \"en\": \"Matcha blended with Okinawa brown sugar.\"}', 49000, 'ly', 1, '2025-12-27 08:42:04'),
(39, 13, '{\"vi\": \"Matcha Latte\", \"en\": \"Matcha Latte\"}', 'matcha-latte', 'assets/client/img/product/matcha-latte.png', '{\"vi\": \"Matcha Nhật Bản hảo hạng kết hợp sữa tươi mịn màng\", \"en\": \"Premium Japanese matcha blended with silky fresh milk.\"}', 55000, 'ly', 1, '2025-12-27 08:42:04'),
(40, 2, '{\"vi\": \"Trà Đào Cam Sả - Nóng\", \"en\": \"Hot Peach Orange Lemongrass Tea\"}', 'tra-dao-cam-sa-nong', 'assets/client/img/product/oolong-tu-quy-sen-nong.png', '{\"vi\": \"Vị thanh ngọt của đào\", \"en\": \"Light, sweet peach flavor.\"}', 59000, 'ly', 1, '2025-12-27 08:42:04'),
(41, 2, '{\"vi\": \"Trà Đào Cam Sả - Đá\", \"en\": \"Iced Peach Orange Lemongrass Tea\"}', 'tra-dao-cam-sa-da', 'assets/client/img/product/tra-dao-cam-sa.png', '{\"vi\": \"Vị thanh ngọt của đào\", \"en\": \"Light, sweet peach flavor.\"}', 49000, 'ly', 1, '2025-12-27 08:42:04'),
(42, 2, '{\"vi\": \"Oolong Tứ Quý Sen (Nóng)\", \"en\": \"Hot Four Seasons Oolong with Lotus Seeds\"}', 'oolong-tu-quy-sen-nong', 'assets/client/img/product/oolong_tu_quy_sen_nong.png', '{\"vi\": \"Nền trà oolong hảo hạng kết hợp cùng hạt sen tươi\", \"en\": \"Premium oolong tea base combined with fresh lotus seeds.\"}', 59000, 'ly', 1, '2025-12-27 08:42:04'),
(43, 2, '{\"vi\": \"Oolong Tứ Quý Sen\", \"en\": \"Four Seasons Oolong with Lotus Seeds\"}', 'oolong-tu-quy-sen', 'assets/client/img/product/oolong_tu_quy_sen_da.png', '{\"vi\": \"Nền trà oolong hảo hạng kết hợp cùng hạt sen tươi\", \"en\": \"Premium oolong tea base combined with fresh lotus seeds.\"}', 49000, 'ly', 1, '2025-12-27 08:42:04'),
(44, 3, '{\"vi\": \"Trà Sữa Oolong Nướng Sương Sáo\", \"en\": \"Roasted Oolong Milk Tea with Grass Jelly\"}', 'tra-sua-oolong-nuong-suong-sao', 'assets/client/img/product/tra_sua_oolong_nuong_suong_sao.png', '{\"vi\": \"Tận hưởng hương vị Oolong nướng đậm đà được Nhà rang kỹ càng\", \"en\": \"Enjoy the rich roasted oolong flavor, carefully roasted by the house.\"}', 55000, 'ly', 1, '2025-12-27 08:42:04'),
(45, 3, '{\"vi\": \"Trà Đen Macchiato\", \"en\": \"Black Tea Macchiato\"}', 'tra-den-macchiato', 'assets/client/img/product/tra_den_macchiato.png', '{\"vi\": \"Trà đen được ủ mới mỗi ngày\", \"en\": \"Black tea brewed fresh daily.\"}', 55000, 'ly', 1, '2025-12-27 08:42:04'),
(46, 3, '{\"vi\": \"Trà Sữa Oolong BLao\", \"en\": \"B’Lao Oolong Milk Tea\"}', 'tra-sua-oolong-blao', 'assets/client/img/product/tra_sua_oolong_blao.png', '{\"vi\": \"Tận hưởng hương vị núi rừng mát lành lắng đọng trong từng ngụm Trà Sữa Oolong B’Lao của Nhà. Từng lá trà được Nhà chắt chiu từ B’Lao (Lâm Đồng)\", \"en\": \"Enjoy the cool mountain flavor in every sip of the house B’Lao Oolong Milk Tea. Each tea leaf is carefully selected from B’Lao (Lam Dong).\"}', 39000, 'ly', 1, '2025-12-27 08:42:04'),
(47, 3, '{\"vi\": \"Hồng Trà Sữa Nóng\", \"en\": \"Hot Black Milk Tea\"}', 'hong-tra-sua-nong', 'assets/client/img/product/hong_tra_sua_nong.png', '{\"vi\": \"Từng ngụm trà chuẩn gu ấm áp\", \"en\": \"Warm, comforting tea in every sip.\"}', 55000, 'ly', 1, '2025-12-27 08:28:02'),
(48, 3, '{\"vi\": \"Hồng Trà Sữa Trân Châu\", \"en\": \"Black Milk Tea with Pearls\"}', 'hong-tra-sua-tran-chau', 'assets/client/img/product/hong_tra_sua_tran_chau.png', '{\"vi\": \"Thêm chút ngọt ngào cho ngày mới với hồng trà nguyên lá\", \"en\": \"Add a touch of sweetness to a new day with whole-leaf black tea.\"}', 55000, 'ly', 1, '2025-12-27 08:29:21'),
(49, 7, '{\"vi\": \"Butter Croissant Sữa Đặc\", \"en\": \"Butter Croissant with Condensed Milk\"}', 'butter-croissant-sua-dac', 'assets/client/img/product/croissant-sua-dac.png', '{\"vi\": \"Bánh Butter Croissant bạn đã yêu\", \"en\": \"The butter croissant you already love.\"}', 35000, 'cái', 1, '2025-12-27 08:42:04'),
(50, 7, '{\"vi\": \"Matcha Burnt Cheesecake\", \"en\": \"Matcha Burnt Cheesecake\"}', 'matcha-burnt-cheesecake', 'assets/client/img/product/matcha-burnt-cheesecake.png', '{\"vi\": \"Hòa quyện giữa phô mai béo ngậy và trà xanh đắng nhẹ\", \"en\": \"A blend of rich cheese and lightly bitter green tea.\"}', 55000, 'cái', 1, '2025-12-27 08:42:04'),
(51, 7, '{\"vi\": \"Burnt Cheesecake\", \"en\": \"Burnt Cheesecake\"}', 'burnt-cheesecake', 'assets/client/img/product/burnt-cheesecake.png', '{\"vi\": \"Phô mai béo mịn\", \"en\": \"Rich, creamy cheese.\"}', 55000, 'cái', 1, '2025-12-27 08:42:04'),
(52, 7, '{\"vi\": \"Mít Sấy\", \"en\": \"Dried Jackfruit\"}', 'mit-say', 'assets/client/img/product/mit-say.png', '{\"vi\": \"Mít sấy khô vàng ươm\", \"en\": \"Golden dried jackfruit.\"}', 20000, 'gói', 1, '2025-12-27 08:42:04'),
(53, 7, '{\"vi\": \"Mochi Kem Trà Sữa Trân Châu\", \"en\": \"Milk Tea Pearl Mochi Ice Cream\"}', 'mochi-kem-tra-sua-tran-chau', 'assets/client/img/product/mochi-tra-sua.png', '{\"vi\": \"Ngoài dẻo thơm\", \"en\": \"Soft and fragrant mochi exterior.\"}', 19000, 'cái', 1, '2025-12-27 08:42:04'),
(54, 7, '{\"vi\": \"Mochi Kem Phúc Bồn Tử\", \"en\": \"Raspberry Mochi Ice Cream\"}', 'mochi-kem-phuc-bon-tu', 'assets/client/img/product/mochi-phuc-bon-tu.png', '{\"vi\": \"Bao bọc bởi lớp vỏ Mochi dẻo thơm\", \"en\": \"Wrapped in a soft, fragrant mochi shell.\"}', 19000, 'cái', 1, '2025-12-27 08:42:04'),
(55, 7, '{\"vi\": \"Mousse Tiramisu\", \"en\": \"Tiramisu Mousse\"}', 'mousse-tiramisu', 'assets/client/img/product/tiramisu.png', '{\"vi\": \"Hương vị dễ ghiền được tạo nên bởi chút đắng nhẹ của cà phê\", \"en\": \"An addictive flavor with a gentle coffee bitterness.\"}', 35000, 'cái', 1, '2025-12-27 08:42:04'),
(56, 7, '{\"vi\": \"Mousse Gấu Chocolate\", \"en\": \"Chocolate Bear Mousse\"}', 'mousse-gau-chocolate', 'assets/client/img/product/mouse-gau-choco.png', '{\"vi\": \"Với vẻ ngoài đáng yêu và hương vị ngọt ngào\", \"en\": \"With a cute look and a sweet flavor.\"}', 39000, 'cái', 1, '2025-12-27 08:42:04'),
(57, 16, '{\"vi\": \"Butter Croissant\", \"en\": \"Butter Croissant\"}', 'butter-croissant', 'assets/client/img/product/croissant.png', '{\"vi\": \"Cắn một miếng\", \"en\": \"Just one bite.\"}', 29000, 'cái', 1, '2025-12-27 08:42:04'),
(58, 16, '{\"vi\": \"Bánh Mì Que Chà Bông Phô Mai Bơ Cay\", \"en\": \"Pork Floss Cheese Spicy Butter Baguette Stick\"}', 'banh-mi-que-cha-bong-pho-mai-bo-cay', 'assets/client/img/product/bmq-cha-bong-pm.png', '{\"vi\": \"Aiiiii Bánh Mì Chà Bông Phô Mai hônggg? Chà bông tơi mịn đẫm phô mai Mozzarella kéo sợi\", \"en\": \"Aiiiii pork floss cheese banh mi, right? Fluffy pork floss soaked in stretchy mozzarella cheese.\"}', 22000, 'cái', 1, '2025-12-27 08:42:04'),
(59, 16, '{\"vi\": \"Bánh Mì Que Bò Nấm Xốt Bơ\", \"en\": \"Beef and Mushroom Butter Sauce Baguette Stick\"}', 'banh-mi-que-bo-nam-xot-bo', 'assets/client/img/product/bmq-bo-nam.png', '{\"vi\": \"Bò mềm thấm vị\", \"en\": \"Tender beef infused with flavor.\"}', 22000, 'cái', 1, '2025-12-27 08:42:04'),
(60, 16, '{\"vi\": \"Bánh Mì Que Pate Cột Đèn\", \"en\": \"Cột Đèn Pate Baguette Stick\"}', 'banh-mi-que-pate-cot-den', 'assets/client/img/product/bmq-pate-hai-phong.png', '{\"vi\": \"Aiiiii Pate Cột Đèn đậm đà thơm béo hônggg? Rộp rộp vỏ bánh nóng hổi giòn rụm\", \"en\": \"Aiiiii rich, creamy Cột Đèn pate, right? Crackly hot crust, crisp and crunchy.\"}', 19000, 'cái', 1, '2025-12-27 08:42:04'),
(61, 16, '{\"vi\": \"Croissant trứng muối\", \"en\": \"Salted Egg Croissant\"}', 'croissant-trung-muoi', 'assets/client/img/product/croissant-trung-muoi.png', '{\"vi\": \"Croissant trứng muối thơm lừng\", \"en\": \"Fragrant salted egg croissant.\"}', 39000, 'cái', 1, '2025-12-27 08:42:04'),
(62, 16, '{\"vi\": \"Chà Bông Phô Mai\", \"en\": \"Pork Floss Cheese Bun\"}', 'cha-bong-pho-mai', 'assets/client/img/product/bami-cha-bong-pho-mai.png', '{\"vi\": \"Chiếc bánh với lớp phô mai vàng sánh mịn bên trong\", \"en\": \"A bun with a smooth, golden cheese layer inside.\"}', 39000, 'cái', 1, '2025-12-27 08:42:04'),
(63, 17, '{\"vi\": \"Pasta Bò Bằm Xốt Bolognese\", \"en\": \"Minced Beef Bolognese Pasta\"}', 'pasta-bo-bam-xot-bolognese', 'assets/client/img/product/bo_bam_xot_bolognese.png', NULL, 59000, 'phần', 1, '2025-12-27 08:42:04'),
(64, 17, '{\"vi\": \"Pasta Heo Nướng Xốt Shoyu Butter\", \"en\": \"Grilled Pork Pasta with Shoyu Butter Sauce\"}', 'pasta-heo-nuong-xot-shoyu-butter', 'assets/client/img/product/heo_nuong_xot_shoyu_butter.png', NULL, 59000, 'phần', 1, '2025-12-27 08:42:04'),
(65, 4, '{\"vi\": \"Floaty Vanilla Mocha\", \"en\": \"Floaty Vanilla Mocha\"}', 'floaty-vanilla-mocha', 'assets/client/img/product/floaty_vanilla_mocha.png', NULL, 65000, 'ly', 1, '2025-12-27 08:42:04'),
(66, 4, '{\"vi\": \"Floaty Bạc Xỉu\", \"en\": \"Bac Xiu Float\"}', 'floaty-bac-xiu', 'assets/client/img/product/floaty_bac_xiu.png', NULL, 65000, 'ly', 1, '2025-12-27 08:42:04'),
(67, 4, '{\"vi\": \"Floaty Matcha Latte\", \"en\": \"Floaty Matcha Latte\"}', 'floaty-matcha-latte', 'assets/client/img/product/floaty_matcha_latte.png', NULL, 65000, 'ly', 1, '2025-12-27 08:42:04'),
(68, 4, '{\"vi\": \"Frappe Chocochip\", \"en\": \"Chocochip Frappe\"}', 'frappe-chocochip', 'assets/client/img/product/choco-chip-frappe.png', '{\"vi\": \"Frappe Choco Chip\", \"en\": \"Choco Chip Frappe.\"}', 65000, 'ly', 1, '2025-12-27 08:42:04'),
(69, 4, '{\"vi\": \"Frappe Matcha Tây Bắc\", \"en\": \"Northwest Matcha Frappe\"}', 'frappe-matcha-tay-bac', 'assets/client/img/product/matcha-frappe.png', '{\"vi\": \"Frappe Matcha kết hợp trà xanh matcha xay mịn với sữa và đá xay\", \"en\": \"Matcha frappe blended with fine matcha green tea, milk, and crushed ice.\"}', 65000, 'ly', 1, '2025-12-27 08:42:04');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `option_groups`
--

CREATE TABLE `option_groups` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(80) NOT NULL,
  `type` varchar(32) NOT NULL,
  `is_required` tinyint(1) NOT NULL DEFAULT 0,
  `min_select` int(11) NOT NULL DEFAULT 0,
  `max_select` int(11) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `option_groups`
--

INSERT INTO `option_groups` (`id`, `name`, `type`, `is_required`, `min_select`, `max_select`) VALUES
(5, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', 'size', 1, 1, 1),
(6, '{\"vi\": \"Topping\", \"en\": \"Topping\"}', 'topping', 0, 0, 4);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `option_values`
--

CREATE TABLE `option_values` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `option_group_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(80) NOT NULL,
  `price_delta` int(11) NOT NULL DEFAULT 0,
  `is_active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `option_values`
--

INSERT INTO `option_values` (`id`, `option_group_id`, `name`, `price_delta`, `is_active`) VALUES
(15, 5, '{\"vi\": \"Vừa\", \"en\": \"Medium\"}', 0, 1),
(16, 5, '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000, 1),
(17, 5, '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000, 1),
(18, 6, '{\"vi\": \"Thạch dừa\", \"en\": \"Coconut Jelly\"}', 3000, 1),
(19, 6, '{\"vi\": \"Sương sáo\", \"en\": \"Grass Jelly\"}', 3000, 1),
(20, 6, '{\"vi\": \"Trân châu đen\", \"en\": \"Black Tapioca Pearls\"}', 5000, 1),
(21, 6, '{\"vi\": \"Trân châu trắng\", \"en\": \"White Tapioca Pearls\"}', 5000, 1),
(22, 6, '{\"vi\": \"Bánh Flan\", \"en\": \"Flan\"}', 7000, 1),
(23, 6, '{\"vi\": \"Thập cẩm\", \"en\": \"Mixed Toppings\"}', 11000, 1),
(24, 6, '{\"vi\": \"Thạch củ năng\", \"en\": \"Water Chestnut Jelly\"}', 5000, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `table_id` bigint(20) UNSIGNED DEFAULT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `booking_id` bigint(20) UNSIGNED DEFAULT NULL,
  `status` enum('open','served','partial_paid','paid','void') NOT NULL DEFAULT 'open',
  `opened_at` datetime NOT NULL DEFAULT current_timestamp(),
  `closed_at` datetime DEFAULT NULL,
  `source` enum('qr','staff_pos','kiosk','web') NOT NULL DEFAULT 'qr'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`id`, `store_id`, `table_id`, `user_id`, `booking_id`, `status`, `opened_at`, `closed_at`, `source`) VALUES
(1, 1, 2, 4, NULL, 'paid', '2025-10-05 09:15:00', '2025-10-05 09:40:00', 'qr'),
(2, 3, NULL, 9, NULL, 'paid', '2025-12-30 14:39:59', NULL, 'web'),
(3, 3, NULL, 9, NULL, 'paid', '2025-12-30 14:40:56', NULL, 'web'),
(4, 3, NULL, 9, NULL, 'paid', '2025-12-30 14:52:03', NULL, 'web'),
(5, 3, NULL, 9, NULL, 'paid', '2025-12-30 15:02:37', NULL, 'web'),
(6, 3, NULL, 9, NULL, 'paid', '2025-12-30 15:03:20', NULL, 'web'),
(7, 3, NULL, 9, NULL, 'paid', '2025-12-30 15:07:38', NULL, 'web'),
(8, 3, NULL, 9, NULL, 'paid', '2025-12-30 21:54:38', NULL, 'web'),
(9, 3, NULL, 9, NULL, 'paid', '2025-12-30 21:54:53', NULL, 'web');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_items`
--

CREATE TABLE `order_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `order_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `qty` int(11) NOT NULL DEFAULT 1,
  `unit_price_snapshot` int(11) NOT NULL,
  `options_price_snapshot` int(11) NOT NULL DEFAULT 0,
  `item_hash` char(64) DEFAULT NULL,
  `item_name_snapshot` varchar(120) DEFAULT NULL,
  `note` varchar(160) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_items`
--

INSERT INTO `order_items` (`id`, `order_id`, `menu_item_id`, `qty`, `unit_price_snapshot`, `options_price_snapshot`, `item_hash`, `item_name_snapshot`, `note`) VALUES
(3, 2, 11, 1, 49000, 10000, '4a6a1bc481917cd1698007df0d46772e1bae85616469528d7e8e8120a438e2fd', '{\"vi\": \"A-Mê Mơ\", \"en\": \"A-Me Apricot\"}', NULL),
(4, 3, 9, 1, 39000, 0, '33012e37b0a52f46646b56911367f55bf7ffa06823a64fffe49b1b36ed901675', '{\"vi\": \"A-Mê Classic\", \"en\": \"A-Me Classic\"}', 'YÊU EM NHIỀU LẮM'),
(5, 4, 9, 1, 39000, 0, 'f007b31f391f8693390a3238af954020b8175021cfe757133dab7021818a510d', '{\"vi\": \"A-Mê Classic\", \"en\": \"A-Me Classic\"}', NULL),
(6, 5, 10, 24, 49000, 6000, '5c3efb5a95addfae20ae45e36932b7e803f590b52b08600dba74b1343c660945', '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', NULL),
(7, 5, 9, 1, 39000, 0, 'f007b31f391f8693390a3238af954020b8175021cfe757133dab7021818a510d', '{\"vi\": \"A-Mê Classic\", \"en\": \"A-Me Classic\"}', NULL),
(8, 5, 13, 1, 45000, 6000, '7f977efcee1cafcd0b7f5bdab32d00e748e22c3a6f9b5c254077dd37884fb15c', '{\"vi\": \"Americano Nóng\", \"en\": \"Hot Americano\"}', NULL),
(9, 5, 10, 1, 49000, 10000, '6c065836cceed4cea742fe91354ee4258a5eb502c5184ca7bde2feb0a2576d7f', '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', NULL),
(10, 5, 19, 1, 65000, 6000, '2d6dd089cdbe453b92af242ad44e08ea82dd048f47b47a3a2fc040bce180eb80', '{\"vi\": \"Caramel Macchiato Đá\", \"en\": \"Iced Caramel Macchiato\"}', 'helo me'),
(11, 5, 14, 1, 45000, 10000, '736d4ba43d18ea4b07b05b5ee6bd0bf662cfc6a681e28e2f4c0ad9a05da4298b', '{\"vi\": \"Espresso Nóng\", \"en\": \"Hot Espresso\"}', NULL),
(12, 6, 13, 1, 45000, 6000, '7f977efcee1cafcd0b7f5bdab32d00e748e22c3a6f9b5c254077dd37884fb15c', '{\"vi\": \"Americano Nóng\", \"en\": \"Hot Americano\"}', NULL),
(13, 6, 36, 1, 45000, 6000, '2d22a71e31968152c9c8f05671cd9954e88dbbff80be36d65579a61ef00202a8', '{\"vi\": \"Matcha Latte Tây Bắc\", \"en\": \"Northwest Matcha Latte\"}', NULL),
(14, 7, 36, 1, 45000, 10000, '1c4f4a62d520bcdf5fa6942e1c6f8656683ae7b55596cc5afbb812b22833977c', '{\"vi\": \"Matcha Latte Tây Bắc\", \"en\": \"Northwest Matcha Latte\"}', NULL),
(15, 8, 10, 4, 49000, 10000, '6c065836cceed4cea742fe91354ee4258a5eb502c5184ca7bde2feb0a2576d7f', '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', NULL),
(16, 9, 10, 1, 49000, 10000, '6c065836cceed4cea742fe91354ee4258a5eb502c5184ca7bde2feb0a2576d7f', '{\"vi\": \"A-Mê Đào\", \"en\": \"A-Me Peach\"}', NULL),
(17, 9, 27, 1, 49000, 10000, 'a53b629ce43ff85dae7bf06fb7127ac4dcbb963b86bd22a3cf09e06ceabaea1f', '{\"vi\": \"Cold Brew Kim Quất\", \"en\": \"Kumquat Cold Brew\"}', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_item_options`
--

CREATE TABLE `order_item_options` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `order_item_id` bigint(20) UNSIGNED NOT NULL,
  `option_value_id` bigint(20) UNSIGNED NOT NULL,
  `option_group_name_snapshot` varchar(80) NOT NULL,
  `option_value_name_snapshot` varchar(80) NOT NULL,
  `price_delta_snapshot` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_item_options`
--

INSERT INTO `order_item_options` (`id`, `order_item_id`, `option_value_id`, `option_group_name_snapshot`, `option_value_name_snapshot`, `price_delta_snapshot`) VALUES
(1, 3, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000),
(2, 6, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(3, 8, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(4, 9, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000),
(5, 10, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(6, 11, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000),
(7, 12, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(8, 13, 16, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Lớn\", \"en\": \"Large\"}', 6000),
(9, 14, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000),
(10, 15, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000),
(11, 16, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000),
(12, 17, 17, '{\"vi\": \"Kích cỡ\", \"en\": \"Size\"}', '{\"vi\": \"Rất lớn\", \"en\": \"Extra Large\"}', 10000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `payments`
--

CREATE TABLE `payments` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `source_type` enum('order','booking') NOT NULL,
  `order_id` bigint(20) UNSIGNED DEFAULT NULL,
  `booking_id` bigint(20) UNSIGNED DEFAULT NULL,
  `user_id` bigint(20) UNSIGNED DEFAULT NULL,
  `method` enum('cash','card','momo','vnpay','zalo','wallet') NOT NULL,
  `type` enum('deposit','prepaid','remaining','refund') NOT NULL,
  `status` enum('pending','paid','failed','refunded') NOT NULL DEFAULT 'pending',
  `amount` int(11) NOT NULL,
  `redeem_points_used` int(11) NOT NULL DEFAULT 0,
  `redeem_value` int(11) NOT NULL DEFAULT 0,
  `paid_at` datetime DEFAULT NULL,
  `txn_ref` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `price_change_request_items`
--

CREATE TABLE `price_change_request_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `requested_price` int(11) NOT NULL,
  `valid_from` datetime NOT NULL,
  `valid_to` datetime NOT NULL,
  `status` enum('pending','approved','rejected','canceled') NOT NULL DEFAULT 'pending',
  `requested_by` bigint(20) UNSIGNED NOT NULL,
  `reviewed_by` bigint(20) UNSIGNED DEFAULT NULL,
  `review_note` varchar(255) DEFAULT NULL,
  `requested_at` datetime NOT NULL DEFAULT current_timestamp(),
  `reviewed_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `review_items`
--

CREATE TABLE `review_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED DEFAULT NULL,
  `order_item_id` bigint(20) UNSIGNED DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `content` text DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `review_stores`
--

CREATE TABLE `review_stores` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `order_id` bigint(20) UNSIGNED DEFAULT NULL,
  `rating` int(11) NOT NULL,
  `content` text DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `review_stores`
--

INSERT INTO `review_stores` (`id`, `user_id`, `store_id`, `order_id`, `rating`, `content`, `created_at`) VALUES
(1, 4, 1, 1, 5, 'Không gian yên tĩnh, phục vụ tốt', '2025-10-05 10:00:00');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role_codes`
--

CREATE TABLE `role_codes` (
  `code` varchar(32) NOT NULL,
  `description` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `role_codes`
--

INSERT INTO `role_codes` (`code`, `description`) VALUES
('customer', 'Customer'),
('owner', 'Store owner'),
('staff', 'Store staff'),
('super_admin', 'System super admin');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `stores`
--

CREATE TABLE `stores` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`name`)),
  `address` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`address`)),
  `latitude` decimal(10,7) NOT NULL,
  `longitude` decimal(10,7) NOT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'open',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `stores`
--

INSERT INTO `stores` (`id`, `name`, `address`, `latitude`, `longitude`, `status`, `created_at`, `updated_at`) VALUES
(1, '{\"vi\": \"ZRS ĐH Nông lâm Thủ Đức\", \"en\": \"ZRS Nong Lam University Thu Duc\"}', '{\"vi\": \"VQCR+GP6, khu phố 6, Thủ Đức, Thành phố Hồ Chí Minh, Việt Nam\", \"en\": \"VQCR+GP6, Ward 6, Thu Duc, Ho Chi Minh City, Vietnam\"}', 10.8670639, 106.7817762, 'open', '2025-12-02 23:16:56', '2026-01-05 16:18:43'),
(2, '{\"vi\": \"ZRS ĐH Ngân hàng Thủ Đức\", \"en\": \"ZRS Banking University Thu Duc\"}', '{\"vi\": \"56 Đ. Hoàng Diệu 2, Linh Chiểu, Thủ Đức, Thành phố Hồ Chí Minh, Việt Nam\", \"en\": \"56 Hoang Dieu 2 St., Linh Chieu, Thu Duc, Ho Chi Minh City, Vietnam\"}', 10.8575142, 106.7444608, 'open', '2025-12-02 23:16:56', '2026-01-05 16:18:56'),
(3, '{\"vi\": \"ZRS Ga Metro Bình Thái\", \"en\": \"ZRS Binh Thai Metro Station\"}', '{\"vi\": \"Trường Thọ, Thủ Đức, Thành phố Hồ Chí Minh, Việt Nam\", \"en\": \"Truong Tho, Thu Duc, Ho Chi Minh City, Vietnam\"}', 10.8321128, 106.7616195, 'open', '2025-12-13 08:51:07', '2026-01-05 16:19:12');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `store_item_price_schedules`
--

CREATE TABLE `store_item_price_schedules` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `price` int(11) NOT NULL,
  `valid_from` datetime NOT NULL,
  `valid_to` datetime NOT NULL,
  `approved_by` bigint(20) UNSIGNED NOT NULL,
  `approved_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `store_item_price_schedules`
--

INSERT INTO `store_item_price_schedules` (`id`, `store_id`, `menu_item_id`, `price`, `valid_from`, `valid_to`, `approved_by`, `approved_at`) VALUES
(2, 1, 9, 24000, '2025-12-28 15:29:53', '2025-12-31 15:29:56', 1, '2025-12-28 15:30:05'),
(3, 2, 12, 26700, '2026-01-06 21:55:56', '2026-01-15 21:56:00', 1, '2026-01-06 21:56:12'),
(4, 2, 26, 34000, '2026-01-06 22:25:38', '2026-01-13 22:25:42', 1, '2026-01-06 22:25:48'),
(5, 2, 17, 52000, '2026-01-06 22:27:15', '2026-01-13 22:27:18', 1, '2026-01-06 22:27:22'),
(6, 2, 19, 34500, '2026-01-06 22:27:49', '2026-01-13 22:27:50', 1, '2026-01-06 22:27:53');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `store_menu_items`
--

CREATE TABLE `store_menu_items` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `menu_item_id` bigint(20) UNSIGNED NOT NULL,
  `in_menu` tinyint(1) NOT NULL DEFAULT 1,
  `availability_status` enum('available','sold_out') NOT NULL DEFAULT 'available',
  `sold_out_until` datetime DEFAULT NULL,
  `sold_out_note` varchar(160) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `store_menu_items`
--

INSERT INTO `store_menu_items` (`id`, `store_id`, `menu_item_id`, `in_menu`, `availability_status`, `sold_out_until`, `sold_out_note`) VALUES
(12, 1, 9, 1, 'available', NULL, NULL),
(13, 1, 10, 1, 'available', NULL, NULL),
(14, 1, 11, 1, 'available', NULL, NULL),
(15, 1, 12, 0, 'available', NULL, NULL),
(16, 1, 13, 1, 'available', NULL, NULL),
(17, 1, 14, 0, 'available', NULL, NULL),
(18, 1, 15, 1, 'available', NULL, NULL),
(19, 1, 16, 1, 'available', NULL, NULL),
(20, 1, 17, 0, 'available', NULL, NULL),
(21, 1, 18, 1, 'sold_out', '2025-12-31 08:46:25', 'Hết nguyên liệu'),
(22, 1, 19, 1, 'available', NULL, NULL),
(23, 1, 20, 0, 'available', NULL, NULL),
(24, 1, 21, 0, 'available', NULL, NULL),
(25, 1, 22, 1, 'available', NULL, NULL),
(26, 1, 23, 1, 'available', NULL, NULL),
(27, 1, 24, 1, 'available', NULL, NULL),
(28, 1, 25, 1, 'available', NULL, NULL),
(29, 1, 26, 1, 'available', NULL, NULL),
(30, 1, 27, 1, 'available', NULL, NULL),
(31, 1, 28, 1, 'available', NULL, NULL),
(32, 1, 29, 1, 'available', NULL, NULL),
(33, 1, 30, 1, 'available', NULL, NULL),
(34, 1, 31, 1, 'available', NULL, NULL),
(35, 1, 32, 1, 'available', NULL, NULL),
(36, 1, 33, 0, 'available', NULL, NULL),
(37, 1, 34, 1, 'available', NULL, NULL),
(38, 1, 35, 1, 'available', NULL, NULL),
(39, 1, 36, 1, 'available', NULL, NULL),
(40, 1, 37, 1, 'available', NULL, NULL),
(41, 1, 38, 1, 'available', NULL, NULL),
(42, 1, 39, 1, 'available', NULL, NULL),
(43, 1, 40, 1, 'available', NULL, NULL),
(44, 1, 41, 0, 'available', NULL, NULL),
(45, 1, 42, 1, 'available', NULL, NULL),
(46, 1, 43, 1, 'sold_out', '2025-12-30 08:46:49', 'Hết nguyên liệu'),
(47, 1, 44, 1, 'available', NULL, NULL),
(48, 1, 45, 1, 'available', NULL, NULL),
(49, 1, 46, 0, 'available', NULL, NULL),
(50, 1, 47, 1, 'available', NULL, NULL),
(51, 1, 48, 1, 'available', NULL, NULL),
(52, 1, 49, 1, 'available', NULL, NULL),
(53, 1, 50, 1, 'available', NULL, NULL),
(54, 1, 51, 1, 'available', NULL, NULL),
(55, 1, 52, 1, 'available', NULL, NULL),
(56, 1, 53, 1, 'available', NULL, NULL),
(57, 1, 54, 1, 'available', NULL, NULL),
(58, 1, 55, 1, 'available', NULL, NULL),
(59, 1, 56, 1, 'available', NULL, NULL),
(60, 1, 57, 1, 'available', NULL, NULL),
(61, 1, 58, 1, 'available', NULL, NULL),
(62, 1, 59, 1, 'available', NULL, NULL),
(63, 1, 60, 1, 'available', NULL, NULL),
(64, 1, 61, 1, 'available', NULL, NULL),
(65, 1, 62, 1, 'available', NULL, NULL),
(66, 1, 63, 1, 'available', NULL, NULL),
(67, 1, 64, 1, 'available', NULL, NULL),
(68, 1, 65, 1, 'available', NULL, NULL),
(69, 1, 66, 1, 'available', NULL, NULL),
(70, 1, 67, 1, 'available', NULL, NULL),
(71, 1, 68, 1, 'available', NULL, NULL),
(72, 1, 69, 1, 'available', NULL, NULL),
(73, 2, 9, 1, 'available', NULL, NULL),
(74, 2, 10, 1, 'available', NULL, NULL),
(75, 2, 11, 1, 'available', NULL, NULL),
(76, 2, 12, 1, 'available', NULL, NULL),
(77, 2, 13, 1, 'available', NULL, NULL),
(78, 2, 14, 1, 'available', NULL, NULL),
(79, 2, 15, 1, 'available', NULL, NULL),
(80, 2, 16, 1, 'available', NULL, NULL),
(81, 2, 17, 1, 'available', NULL, NULL),
(82, 2, 18, 1, 'available', NULL, NULL),
(83, 2, 19, 1, 'available', NULL, NULL),
(84, 2, 20, 1, 'available', NULL, NULL),
(85, 2, 21, 1, 'available', NULL, NULL),
(86, 2, 22, 1, 'available', NULL, NULL),
(87, 2, 23, 1, 'available', NULL, NULL),
(88, 2, 24, 1, 'available', NULL, NULL),
(89, 2, 25, 1, 'available', NULL, NULL),
(90, 2, 26, 1, 'available', NULL, NULL),
(91, 2, 27, 1, 'available', NULL, NULL),
(92, 2, 28, 1, 'available', NULL, NULL),
(93, 2, 29, 1, 'available', NULL, NULL),
(94, 2, 30, 1, 'available', NULL, NULL),
(95, 2, 31, 1, 'available', NULL, NULL),
(96, 2, 32, 1, 'available', NULL, NULL),
(97, 2, 33, 1, 'sold_out', '2025-12-30 08:50:16', 'Hết nguyên liệu'),
(98, 2, 34, 1, 'available', NULL, NULL),
(99, 2, 35, 1, 'available', NULL, NULL),
(100, 2, 36, 1, 'available', NULL, NULL),
(101, 2, 37, 1, 'available', NULL, NULL),
(102, 2, 38, 1, 'available', NULL, NULL),
(103, 2, 39, 1, 'available', NULL, NULL),
(104, 2, 40, 1, 'available', NULL, NULL),
(105, 2, 41, 1, 'available', NULL, NULL),
(106, 2, 42, 1, 'available', NULL, NULL),
(107, 2, 43, 1, 'available', NULL, NULL),
(108, 2, 44, 1, 'available', NULL, NULL),
(109, 2, 45, 1, 'available', NULL, NULL),
(110, 2, 46, 1, 'available', NULL, NULL),
(111, 2, 47, 1, 'available', NULL, NULL),
(112, 2, 48, 1, 'available', NULL, NULL),
(113, 2, 49, 1, 'available', NULL, NULL),
(114, 2, 50, 1, 'available', NULL, NULL),
(115, 2, 51, 1, 'available', NULL, NULL),
(116, 2, 52, 1, 'available', NULL, NULL),
(117, 2, 53, 1, 'available', NULL, NULL),
(118, 2, 54, 1, 'available', NULL, NULL),
(119, 2, 55, 1, 'available', NULL, NULL),
(120, 2, 56, 1, 'available', NULL, NULL),
(121, 2, 57, 1, 'available', NULL, NULL),
(122, 2, 58, 1, 'available', NULL, NULL),
(123, 2, 59, 1, 'available', NULL, NULL),
(124, 2, 60, 1, 'available', NULL, NULL),
(125, 2, 61, 1, 'available', NULL, NULL),
(126, 2, 62, 1, 'available', NULL, NULL),
(127, 2, 63, 1, 'available', NULL, NULL),
(128, 2, 64, 1, 'available', NULL, NULL),
(129, 2, 65, 1, 'available', NULL, NULL),
(130, 2, 66, 1, 'available', NULL, NULL),
(131, 2, 67, 1, 'available', NULL, NULL),
(132, 2, 68, 1, 'available', NULL, NULL),
(133, 2, 69, 1, 'available', NULL, NULL),
(134, 3, 9, 1, 'available', NULL, NULL),
(135, 3, 10, 1, 'available', NULL, NULL),
(136, 3, 11, 1, 'available', NULL, NULL),
(137, 3, 12, 1, 'available', NULL, NULL),
(138, 3, 13, 1, 'available', NULL, NULL),
(139, 3, 14, 1, 'available', NULL, NULL),
(140, 3, 15, 1, 'available', NULL, NULL),
(141, 3, 16, 1, 'available', NULL, NULL),
(142, 3, 17, 1, 'available', NULL, NULL),
(143, 3, 18, 1, 'available', NULL, NULL),
(144, 3, 19, 1, 'available', NULL, NULL),
(145, 3, 20, 1, 'available', NULL, NULL),
(146, 3, 21, 1, 'available', NULL, NULL),
(147, 3, 22, 1, 'available', NULL, NULL),
(148, 3, 23, 0, 'available', NULL, NULL),
(149, 3, 24, 1, 'available', NULL, NULL),
(150, 3, 25, 1, 'available', NULL, NULL),
(151, 3, 26, 0, 'available', NULL, NULL),
(152, 3, 27, 1, 'available', NULL, NULL),
(153, 3, 28, 1, 'available', NULL, NULL),
(154, 3, 29, 0, 'available', NULL, NULL),
(155, 3, 30, 1, 'available', NULL, NULL),
(156, 3, 31, 1, 'available', NULL, NULL),
(157, 3, 32, 1, 'available', NULL, NULL),
(158, 3, 33, 1, 'available', NULL, NULL),
(159, 3, 34, 1, 'available', NULL, NULL),
(160, 3, 35, 0, 'available', NULL, NULL),
(161, 3, 36, 1, 'available', NULL, NULL),
(162, 3, 37, 1, 'available', NULL, NULL),
(163, 3, 38, 1, 'available', NULL, NULL),
(164, 3, 39, 1, 'available', NULL, NULL),
(165, 3, 40, 0, 'available', NULL, NULL),
(166, 3, 41, 1, 'available', NULL, NULL),
(167, 3, 42, 1, 'available', NULL, NULL),
(168, 3, 43, 1, 'available', NULL, NULL),
(169, 3, 44, 0, 'available', NULL, NULL),
(170, 3, 45, 1, 'available', NULL, NULL),
(171, 3, 46, 1, 'available', NULL, NULL),
(172, 3, 47, 1, 'available', NULL, NULL),
(173, 3, 48, 1, 'available', NULL, NULL),
(174, 3, 49, 1, 'available', NULL, NULL),
(175, 3, 50, 1, 'available', NULL, NULL),
(176, 3, 51, 0, 'available', NULL, NULL),
(177, 3, 52, 1, 'available', NULL, NULL),
(178, 3, 53, 0, 'available', NULL, NULL),
(179, 3, 54, 1, 'available', NULL, NULL),
(180, 3, 55, 1, 'available', NULL, NULL),
(181, 3, 56, 1, 'available', NULL, NULL),
(182, 3, 57, 0, 'available', NULL, NULL),
(183, 3, 58, 1, 'available', NULL, NULL),
(184, 3, 59, 1, 'available', NULL, NULL),
(185, 3, 60, 1, 'available', NULL, NULL),
(186, 3, 61, 1, 'available', NULL, NULL),
(187, 3, 62, 1, 'available', NULL, NULL),
(188, 3, 63, 1, 'available', NULL, NULL),
(189, 3, 64, 1, 'available', NULL, NULL),
(190, 3, 65, 1, 'available', NULL, NULL),
(191, 3, 66, 0, 'available', NULL, NULL),
(192, 3, 67, 1, 'available', NULL, NULL),
(193, 3, 68, 1, 'available', NULL, NULL),
(194, 3, 69, 1, 'available', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `store_option_values`
--

CREATE TABLE `store_option_values` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `option_value_id` bigint(20) UNSIGNED NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `availability_status` enum('available','sold_out') NOT NULL DEFAULT 'available',
  `sold_out_until` datetime DEFAULT NULL,
  `note` varchar(160) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `store_option_values`
--

INSERT INTO `store_option_values` (`id`, `store_id`, `option_value_id`, `is_active`, `availability_status`, `sold_out_until`, `note`) VALUES
(2, 1, 22, 1, 'sold_out', '2026-01-12 15:50:50', '{\"vi\" : \"Hết hàng tạm thời\", \"en\" : \"Temporarily out of stock\"}');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tables_`
--

CREATE TABLE `tables_` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `zone_id` bigint(20) UNSIGNED NOT NULL,
  `table_uid` varchar(40) NOT NULL,
  `capacity` int(11) NOT NULL DEFAULT 2,
  `chair_type` varchar(32) DEFAULT NULL,
  `position_note` varchar(120) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `tables_`
--

INSERT INTO `tables_` (`id`, `zone_id`, `table_uid`, `capacity`, `chair_type`, `position_note`) VALUES
(1, 1, 'A-Q1', 2, 'ergonomic', 'Vách ngăn'),
(2, 2, 'A-N3', 4, 'normal', 'Gần quầy');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `email` varchar(190) DEFAULT NULL,
  `username` varchar(64) NOT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `oauth_provider` varchar(32) DEFAULT NULL,
  `oauth_id` varchar(190) DEFAULT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'active',
  `is_super_admin` tinyint(1) NOT NULL DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `email`, `username`, `password_hash`, `oauth_provider`, `oauth_id`, `status`, `is_super_admin`, `created_at`, `updated_at`) VALUES
(1, 'super@0all.app', 'superadmin', '***', NULL, NULL, 'active', 1, '2025-12-02 23:16:56', '2025-12-02 23:16:56'),
(2, 'ownerA@0all.app', 'ownerA', '***', NULL, NULL, 'active', 0, '2025-12-02 23:16:56', '2025-12-02 23:16:56'),
(3, 'staffA@0all.app', 'staffA', '***', NULL, NULL, 'active', 0, '2025-12-02 23:16:56', '2025-12-02 23:16:56'),
(4, 'khanh@gmail.com', 'khanhng', NULL, 'google', '1001', 'active', 0, '2025-12-02 23:16:56', '2025-12-02 23:16:56'),
(9, 'trungvan.me@gmail.com', 'trungvan.me', '$argon2id$v=19$m=65536,t=3,p=1$qs1g4HULzCASXWrFywkbsQ$FTp7bJugsicTm/VNAf1FEBdLGjQdIJbeQq1zH1Gt9zc', NULL, NULL, 'ACTIVE', 0, '2025-12-28 09:19:41', '2025-12-28 09:19:41'),
(11, 'trungvan.me@gmail.xyz', 'trungvan_me_8628', '$argon2id$v=19$m=65536,t=3,p=1$RIotQQg9Jiml6mCVQWOSTg$k1dQGMT8XpcNKC7A02SJXi1aiPqFnPzue+0swPU9csg', NULL, NULL, 'ACTIVE', 0, '2025-12-28 09:31:05', '2025-12-28 09:31:05'),
(12, 'trungx.me@gmail.com', 'trungx_me_9ed4', '$argon2id$v=19$m=65536,t=3,p=1$vdQRatNeQfmD2siD3S9CSA$wMN34DlQBAB3Bs+drsC0YHNfgKrjPI+MonmjOKbnVPA', NULL, NULL, 'ACTIVE', 0, '2026-01-03 10:37:50', '2026-01-03 10:37:50'),
(13, 'vantrung@gmail.com', 'vantrung_d6a0', '$argon2id$v=19$m=65536,t=3,p=1$Bw/4RNyTbwfUGtJ6GRlp4A$CA7+FGVf7TrZ/nCmgYwPiIB78bY74Cv7aCW8mANb7og', NULL, NULL, 'ACTIVE', 0, '2026-01-03 11:48:04', '2026-01-03 11:48:04'),
(14, 'abcxyz@gmail.com', 'abcxyz_387d', '$argon2id$v=19$m=65536,t=3,p=1$DEsejMRx1Nx0QjPkHkLgOA$XCwV4lNxOYIckaiS69+9opuZ61PfDmB+XvAumOuYmZw', NULL, NULL, 'ACTIVE', 0, '2026-01-06 10:31:30', '2026-01-06 10:31:30');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user_store_roles`
--

CREATE TABLE `user_store_roles` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `role_code` varchar(32) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `user_store_roles`
--

INSERT INTO `user_store_roles` (`id`, `user_id`, `store_id`, `role_code`, `created_at`) VALUES
(1, 2, 1, 'owner', '2025-12-02 23:16:56'),
(2, 3, 1, 'staff', '2025-12-02 23:16:56'),
(3, 2, 2, 'owner', '2025-12-02 23:16:56');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `zones`
--

CREATE TABLE `zones` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `store_id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(80) NOT NULL,
  `seat_fee_type` enum('hourly','fixed','none') NOT NULL DEFAULT 'hourly',
  `seat_fee_value` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `zones`
--

INSERT INTO `zones` (`id`, `store_id`, `name`, `seat_fee_type`, `seat_fee_value`) VALUES
(1, 1, 'Lặng lẽ', 'hourly', 15000),
(2, 1, 'Ồn ào', 'hourly', 10000);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `auth_tokens`
--
ALTER TABLE `auth_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_auth_tokens_auth_hash` (`auth_hash`),
  ADD KEY `idx_auth_tokens_user_status` (`user_id`,`status`),
  ADD KEY `idx_auth_tokens_hash_device_status` (`auth_hash`,`device_id`,`status`);

--
-- Chỉ mục cho bảng `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_booking_store` (`store_id`),
  ADD KEY `fk_booking_table` (`table_id`),
  ADD KEY `fk_booking_user` (`user_id`);

--
-- Chỉ mục cho bảng `booking_items`
--
ALTER TABLE `booking_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_bi_booking` (`booking_id`),
  ADD KEY `fk_bi_item` (`menu_item_id`);

--
-- Chỉ mục cho bảng `carts`
--
ALTER TABLE `carts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_cart_user_store` (`user_id`,`store_id`),
  ADD KEY `idx_cart_store` (`store_id`);

--
-- Chỉ mục cho bảng `cart_items`
--
ALTER TABLE `cart_items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_cart_item_hash` (`cart_id`,`item_hash`),
  ADD KEY `idx_cart_item_menu` (`menu_item_id`);

--
-- Chỉ mục cho bảng `cart_item_options`
--
ALTER TABLE `cart_item_options`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_cart_item_option` (`cart_item_id`,`option_value_id`),
  ADD KEY `fk_cart_item_option_value` (`option_value_id`);

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `slug` (`slug`),
  ADD KEY `idx_categories_slug` (`slug`);

--
-- Chỉ mục cho bảng `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_invoice_number` (`invoice_number`),
  ADD KEY `idx_invoice_order` (`order_id`),
  ADD KEY `idx_invoice_store` (`store_id`),
  ADD KEY `fk_invoice_user` (`user_id`);

--
-- Chỉ mục cho bảng `item_option_groups`
--
ALTER TABLE `item_option_groups`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_item_group` (`menu_item_id`,`option_group_id`),
  ADD KEY `fk_iog_group` (`option_group_id`);

--
-- Chỉ mục cho bảng `loyalty_accounts`
--
ALTER TABLE `loyalty_accounts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Chỉ mục cho bảng `loyalty_transactions`
--
ALTER TABLE `loyalty_transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_lt_user` (`user_id`),
  ADD KEY `fk_lt_store` (`store_id`),
  ADD KEY `fk_lt_payment` (`payment_id`);

--
-- Chỉ mục cho bảng `menu_items`
--
ALTER TABLE `menu_items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `slug` (`slug`),
  ADD KEY `fk_menu_category` (`category_id`),
  ADD KEY `idx_menu_items_slug` (`slug`);

--
-- Chỉ mục cho bảng `option_groups`
--
ALTER TABLE `option_groups`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `option_values`
--
ALTER TABLE `option_values`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_optval_group` (`option_group_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_order_store` (`store_id`),
  ADD KEY `fk_order_table` (`table_id`),
  ADD KEY `fk_order_user` (`user_id`),
  ADD KEY `fk_order_booking` (`booking_id`);

--
-- Chỉ mục cho bảng `order_items`
--
ALTER TABLE `order_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_oi_order` (`order_id`),
  ADD KEY `fk_oi_item` (`menu_item_id`);

--
-- Chỉ mục cho bảng `order_item_options`
--
ALTER TABLE `order_item_options`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_order_item_option` (`order_item_id`,`option_value_id`),
  ADD KEY `fk_order_item_option_value` (`option_value_id`);

--
-- Chỉ mục cho bảng `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_pay_order` (`order_id`),
  ADD KEY `fk_pay_booking` (`booking_id`),
  ADD KEY `fk_pay_user` (`user_id`);

--
-- Chỉ mục cho bảng `price_change_request_items`
--
ALTER TABLE `price_change_request_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_prq_store` (`store_id`),
  ADD KEY `fk_prq_item` (`menu_item_id`),
  ADD KEY `fk_prq_reqby` (`requested_by`),
  ADD KEY `fk_prq_revby` (`reviewed_by`);

--
-- Chỉ mục cho bảng `review_items`
--
ALTER TABLE `review_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_ri_user` (`user_id`),
  ADD KEY `fk_ri_item` (`menu_item_id`),
  ADD KEY `fk_ri_store` (`store_id`),
  ADD KEY `fk_ri_oi` (`order_item_id`);

--
-- Chỉ mục cho bảng `review_stores`
--
ALTER TABLE `review_stores`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_rs_user` (`user_id`),
  ADD KEY `fk_rs_store` (`store_id`),
  ADD KEY `fk_rs_order` (`order_id`);

--
-- Chỉ mục cho bảng `role_codes`
--
ALTER TABLE `role_codes`
  ADD PRIMARY KEY (`code`);

--
-- Chỉ mục cho bảng `stores`
--
ALTER TABLE `stores`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `store_item_price_schedules`
--
ALTER TABLE `store_item_price_schedules`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_price_window` (`store_id`,`menu_item_id`,`valid_from`,`valid_to`),
  ADD KEY `fk_sips_item` (`menu_item_id`),
  ADD KEY `fk_sips_approved` (`approved_by`);

--
-- Chỉ mục cho bảng `store_menu_items`
--
ALTER TABLE `store_menu_items`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_store_item` (`store_id`,`menu_item_id`),
  ADD KEY `fk_smi_item` (`menu_item_id`);

--
-- Chỉ mục cho bảng `store_option_values`
--
ALTER TABLE `store_option_values`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_store_optval` (`store_id`,`option_value_id`),
  ADD KEY `fk_sov_optval` (`option_value_id`);

--
-- Chỉ mục cho bảng `tables_`
--
ALTER TABLE `tables_`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_zone_tableuid` (`zone_id`,`table_uid`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Chỉ mục cho bảng `user_store_roles`
--
ALTER TABLE `user_store_roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_user_store` (`user_id`,`store_id`),
  ADD KEY `idx_role` (`role_code`),
  ADD KEY `fk_usr_store` (`store_id`);

--
-- Chỉ mục cho bảng `zones`
--
ALTER TABLE `zones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_zone_store` (`store_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `auth_tokens`
--
ALTER TABLE `auth_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=185;

--
-- AUTO_INCREMENT cho bảng `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `booking_items`
--
ALTER TABLE `booking_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `carts`
--
ALTER TABLE `carts`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `cart_items`
--
ALTER TABLE `cart_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT cho bảng `cart_item_options`
--
ALTER TABLE `cart_item_options`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `invoices`
--
ALTER TABLE `invoices`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `item_option_groups`
--
ALTER TABLE `item_option_groups`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT cho bảng `loyalty_accounts`
--
ALTER TABLE `loyalty_accounts`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `loyalty_transactions`
--
ALTER TABLE `loyalty_transactions`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `menu_items`
--
ALTER TABLE `menu_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;

--
-- AUTO_INCREMENT cho bảng `option_groups`
--
ALTER TABLE `option_groups`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `option_values`
--
ALTER TABLE `option_values`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `order_items`
--
ALTER TABLE `order_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT cho bảng `order_item_options`
--
ALTER TABLE `order_item_options`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT cho bảng `payments`
--
ALTER TABLE `payments`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `price_change_request_items`
--
ALTER TABLE `price_change_request_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `review_items`
--
ALTER TABLE `review_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `review_stores`
--
ALTER TABLE `review_stores`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `stores`
--
ALTER TABLE `stores`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `store_item_price_schedules`
--
ALTER TABLE `store_item_price_schedules`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `store_menu_items`
--
ALTER TABLE `store_menu_items`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=195;

--
-- AUTO_INCREMENT cho bảng `store_option_values`
--
ALTER TABLE `store_option_values`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `tables_`
--
ALTER TABLE `tables_`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `user_store_roles`
--
ALTER TABLE `user_store_roles`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT cho bảng `zones`
--
ALTER TABLE `zones`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `auth_tokens`
--
ALTER TABLE `auth_tokens`
  ADD CONSTRAINT `fk_auth_tokens_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `fk_booking_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_booking_table` FOREIGN KEY (`table_id`) REFERENCES `tables_` (`id`),
  ADD CONSTRAINT `fk_booking_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `booking_items`
--
ALTER TABLE `booking_items`
  ADD CONSTRAINT `fk_bi_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `fk_bi_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`);

--
-- Các ràng buộc cho bảng `carts`
--
ALTER TABLE `carts`
  ADD CONSTRAINT `fk_cart_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `cart_items`
--
ALTER TABLE `cart_items`
  ADD CONSTRAINT `fk_cart_item_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cart_item_menu` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`);

--
-- Các ràng buộc cho bảng `cart_item_options`
--
ALTER TABLE `cart_item_options`
  ADD CONSTRAINT `fk_cart_item_option_item` FOREIGN KEY (`cart_item_id`) REFERENCES `cart_items` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_cart_item_option_value` FOREIGN KEY (`option_value_id`) REFERENCES `option_values` (`id`);

--
-- Các ràng buộc cho bảng `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `fk_invoice_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `fk_invoice_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_invoice_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `item_option_groups`
--
ALTER TABLE `item_option_groups`
  ADD CONSTRAINT `fk_iog_group` FOREIGN KEY (`option_group_id`) REFERENCES `option_groups` (`id`),
  ADD CONSTRAINT `fk_iog_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`);

--
-- Các ràng buộc cho bảng `loyalty_accounts`
--
ALTER TABLE `loyalty_accounts`
  ADD CONSTRAINT `fk_la_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `loyalty_transactions`
--
ALTER TABLE `loyalty_transactions`
  ADD CONSTRAINT `fk_lt_payment` FOREIGN KEY (`payment_id`) REFERENCES `payments` (`id`),
  ADD CONSTRAINT `fk_lt_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_lt_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `menu_items`
--
ALTER TABLE `menu_items`
  ADD CONSTRAINT `fk_menu_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);

--
-- Các ràng buộc cho bảng `option_values`
--
ALTER TABLE `option_values`
  ADD CONSTRAINT `fk_optval_group` FOREIGN KEY (`option_group_id`) REFERENCES `option_groups` (`id`);

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_order_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `fk_order_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_order_table` FOREIGN KEY (`table_id`) REFERENCES `tables_` (`id`),
  ADD CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `order_items`
--
ALTER TABLE `order_items`
  ADD CONSTRAINT `fk_oi_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`),
  ADD CONSTRAINT `fk_oi_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

--
-- Các ràng buộc cho bảng `order_item_options`
--
ALTER TABLE `order_item_options`
  ADD CONSTRAINT `fk_order_item_option_item` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_order_item_option_value` FOREIGN KEY (`option_value_id`) REFERENCES `option_values` (`id`);

--
-- Các ràng buộc cho bảng `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `fk_pay_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  ADD CONSTRAINT `fk_pay_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `fk_pay_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `price_change_request_items`
--
ALTER TABLE `price_change_request_items`
  ADD CONSTRAINT `fk_prq_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`),
  ADD CONSTRAINT `fk_prq_reqby` FOREIGN KEY (`requested_by`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_prq_revby` FOREIGN KEY (`reviewed_by`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_prq_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`);

--
-- Các ràng buộc cho bảng `review_items`
--
ALTER TABLE `review_items`
  ADD CONSTRAINT `fk_ri_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`),
  ADD CONSTRAINT `fk_ri_oi` FOREIGN KEY (`order_item_id`) REFERENCES `order_items` (`id`),
  ADD CONSTRAINT `fk_ri_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_ri_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `review_stores`
--
ALTER TABLE `review_stores`
  ADD CONSTRAINT `fk_rs_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  ADD CONSTRAINT `fk_rs_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_rs_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `store_item_price_schedules`
--
ALTER TABLE `store_item_price_schedules`
  ADD CONSTRAINT `fk_sips_approved` FOREIGN KEY (`approved_by`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `fk_sips_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`),
  ADD CONSTRAINT `fk_sips_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`);

--
-- Các ràng buộc cho bảng `store_menu_items`
--
ALTER TABLE `store_menu_items`
  ADD CONSTRAINT `fk_smi_item` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`),
  ADD CONSTRAINT `fk_smi_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`);

--
-- Các ràng buộc cho bảng `store_option_values`
--
ALTER TABLE `store_option_values`
  ADD CONSTRAINT `fk_sov_optval` FOREIGN KEY (`option_value_id`) REFERENCES `option_values` (`id`),
  ADD CONSTRAINT `fk_sov_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`);

--
-- Các ràng buộc cho bảng `tables_`
--
ALTER TABLE `tables_`
  ADD CONSTRAINT `fk_table_zone` FOREIGN KEY (`zone_id`) REFERENCES `zones` (`id`);

--
-- Các ràng buộc cho bảng `user_store_roles`
--
ALTER TABLE `user_store_roles`
  ADD CONSTRAINT `fk_usr_role` FOREIGN KEY (`role_code`) REFERENCES `role_codes` (`code`),
  ADD CONSTRAINT `fk_usr_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`),
  ADD CONSTRAINT `fk_usr_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `zones`
--
ALTER TABLE `zones`
  ADD CONSTRAINT `fk_zone_store` FOREIGN KEY (`store_id`) REFERENCES `stores` (`id`);

-- --------------------------------------------------------
--
-- Cau truc bang cho bang `password_reset_tokens`
--
CREATE TABLE `password_reset_tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `user_id` bigint(20) UNSIGNED NOT NULL,
  `token_hash` char(64) NOT NULL,
  `expires_at` datetime NOT NULL,
  `used_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `request_ip` varchar(45) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Chi muc cho bang `password_reset_tokens`
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uk_prt_token_hash` (`token_hash`),
  ADD KEY `idx_prt_user` (`user_id`);

-- AUTO_INCREMENT cho bang `password_reset_tokens`
ALTER TABLE `password_reset_tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

-- Cac rang buoc cho bang `password_reset_tokens`
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `fk_prt_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
