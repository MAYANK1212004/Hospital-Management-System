-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 18, 2025 at 06:39 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `hospital_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin_users`
--

CREATE TABLE `admin_users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `failed_attempts` int(11) DEFAULT 0,
  `lock_time` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `admin_users`
--

INSERT INTO `admin_users` (`id`, `name`, `email`, `password_hash`, `failed_attempts`, `lock_time`, `created_at`) VALUES
(1, 'Mayank', 'mayankdua12104@gmail.com', '11e4a9f218eb0359cf60e11cf86b93de132939b0be3ca77e33bd01c174140d52', 0, NULL, '2025-06-04 04:48:07');

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

CREATE TABLE `appointments` (
  `appointment_id` int(11) NOT NULL,
  `patient_id` varchar(20) NOT NULL,
  `doctor_id` varchar(20) NOT NULL,
  `appointment_date` date NOT NULL,
  `appointment_time` time NOT NULL,
  `reason` text DEFAULT NULL,
  `booking_timestamp` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(20) DEFAULT 'Scheduled'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`appointment_id`, `patient_id`, `doctor_id`, `appointment_date`, `appointment_time`, `reason`, `booking_timestamp`, `status`) VALUES
(16, 'PAT97125', 'DOC2420', '2025-07-17', '15:00:00', 'fever', '2025-07-17 09:31:20', 'Confirmed');

-- --------------------------------------------------------

--
-- Table structure for table `bill_log`
--

CREATE TABLE `bill_log` (
  `id` int(11) NOT NULL,
  `patient_id` varchar(50) DEFAULT NULL,
  `bill_date` date DEFAULT NULL,
  `amount` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bill_log`
--

INSERT INTO `bill_log` (`id`, `patient_id`, `bill_date`, `amount`) VALUES
(2, 'PAT88073', '2025-06-19', 50);

-- --------------------------------------------------------

--
-- Table structure for table `diagnosis`
--

CREATE TABLE `diagnosis` (
  `MedID` int(11) NOT NULL,
  `Patientname` varchar(100) DEFAULT NULL,
  `Diagnosis` varchar(100) DEFAULT NULL,
  `Medicine` varchar(100) DEFAULT NULL,
  `Quantity` varchar(100) DEFAULT NULL,
  `Ward` varchar(4) DEFAULT NULL,
  `ID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `doctor`
--

CREATE TABLE `doctor` (
  `doctor_id` varchar(30) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `qualification` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `image` longblob DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `speciality` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `doctor`
--

INSERT INTO `doctor` (`doctor_id`, `name`, `password`, `gender`, `qualification`, `email`, `image`, `created_at`, `speciality`) VALUES
('DOC2420', 'Mayank Duapanjabi', 'Mayank2004@', 'Male', 'MBBS', 'aroramayank488@gmail.com', 0x89504e470d0a1a0a0000000d4948445200000064000000500806000000ef09d0ef000000017352474200aece1ce90000000467414d410000b18f0bfc6105000000097048597300000ec300000ec301c76fa86400000f2549444154785eed9d7b8c94d519c6cf99d95d96057159178a8a82a005ef58b540b08ab44061bdd154abd64ada46c31f6d53d35b5293a6356dd25893b6897f349826d23f8cb77a4b60b9498bc52a45bc542b50da159422cbc27291bdefcee9f33b674696dd9965869d999d61e749de3ddf6567e69ce73def7b6eeff93e5342092594503cb0f1b4a0e1162eb4a6bbdb2076e34617bfece1eebcd39a9e9e70128d1afbe49327debfe1066bcaca0c62d7ac39e15e21a22015e2162db22616b32612b1c6b9a8d272636dd41f8b762fb15844d712e23fa7fb48cc4b2412d31534d5a3fb3dbad6a3cf74f9e358cce9beb3f5f505a7a08252889b38d19a193344610f845788b44a1179a6d2b344648dfea546e763958ed5fdd1ba5629a99044c2177865744adaf59963ba7248f70e296dd6b5667de6a0ce8f28e57ea72caac7bcfdb6b17bf6148c62864c21de0d1d38604c55953523478e10396345d26411364ee47dc68bb5637d6acc2809e48fd0f9081d93962bc55a4eb41063826558dba5b4436987ae77e8b85dd2a2f3469da3a8c6f8719314b44b95e090696beb30adadced4d60e997b2b048544a490d152c83952c8d522698a489a2ab940c728e65cfd3b4a08c43b479e71657c4d028993e3e5094a724a5dfc7f51140afa9fce51c60792ffeab8410ad92a85ec95428e4921b161a510b50fb898319e7ce7e6e9d2d5928b748c65407c224fb9ce5b82709486a29a74bc53b255c71bbc929c3baa76a6d3ff579e901785b879f322a6b2b25a051dafd38971b95405be58e90512dcd2193ac70d25902f85e897bc7bfb44d228c172b629fd97648f17e7f69bf6f6c376c306ac2ca7c8b942bc6beaea2a97422e5641afd3a5b912ace37c09bf9fc8435e2ac700e86d31c8873ade2af9abf2b9490ad966cacbbb72edca724282effbd3504722e354b08b55a0fb75f902a5e3948e96d048630d43ad84548074aca645724c65c09d6139cb55866d72674d7400fa8e89b281ac13e2c710c68c51e6714f53945ea142dca7e373948e54cafd4255445f4038d6d2a674afd2c754867feab841e97ea5b43159554a56897133675a5353334219ff924e6f93d0689fa7cc6ba87c1ac0da6efdfd48b241f282cab5de343777d8cd9bb3a694ac29c4d5d58d5286714bb749ae954cd7e57324559230702b7ed0a8b74ab096ed922d1214f3815db912f73668649328baac2860bee40b928b24b417a78b320065a14c948d325256ca4cd9b382415948bcf11ea3411dbda7af4aee508397682786039813a37d795af2ac06979bd4d81f1d4c633fb8da5b553556ca9829939dad3346d7a7475b9109429929fb6ccf059c0c02a7ac1077df7d580103bcaf4bee5586e6482a743c5cac03308553e1cb0e07818b89716e4e09197ff0d36ead31d365aebf52661867d4ea1c650c6774aa813f20619cf2a0ceb74b32ee16676e21ccb88631c6a5926912a6c57b4f790c57c0015cc0c9a59e23b8ca101929c4dd730fd63159729b7eec97aa09e7ca3a2a753e9cdc542ae0be2a3d277013c66193e39ca58db415e2e6cfb7e6e041da0cba7ab7eac7cf545a52447fa018b8b955321fce3c776922130be14be94130437ba584ee6d09c9013770045770965d85b8c993adba74d3d488ff54a74b5403187d17bb758479aadc002b81a3259e3371e7394c032755889b33c79a4993a2f28b9fd7e9d992621ff831fdc1eced7b92b725ac83e44231700457677beec4a1e7f22448c74222a6bc9c55bebba4751695e8f216af42982064e9d6dad53a7b5ee97e492eac052b19e339833b384c83ef01898d8f39e8e2de20d37b543d081aab425ec74885407858197c5cf2a28862559075fa2b54b6dfaa6cacdf67bbc7c8ef76e9fb8974f98e8e374af60f34361958633d3ddc3f43996632ed0c09511ec508dc146141bb25ef8af89d22e8631d13227440926debe80d383bce61e0342552de7413268486c9da0b2577a90009932b3eebb0b645b5f41d1dffc274762ed77983ce715d58fc352a278b67bd032cb205be2fe2b98343b814a79edb1448adadda5ac22f27e8e812098d395f528ccad8a7748d88c055bd2e4510b3a592d8a93affb2527a8d4c10e6aa6c09dee0f012cf29dca6406a858c1e4da669c0f9226a52b1e2a8caf1be487fcd34367e602a2a5012d759df67fae72addcb87e5c3213dae319edb1448aa1077edb5d68c19830933b8999ba70c670bb4073115bc53d6f0bc8ebf25bffdb0696ddd6eb76e65fda25c2e64b1aeff40b24cc7b093ebb2e1fee190889b2be1d6739c04c92d64a4bacf910891215808039c62020df811096dc6df248c39682f880f238afe429d5f25611a28df950c2e09121ce5394e82e40a1931c2aa0035920912e26b73d90bc926c8276d04edc68b92e5a6aaaac1ae5e8db5709f1e0fab9b5f51998886c99fe5c3215c064e6b3cc74990542176dd3a6a59adbe64bc84ee6ebe6bd2a900374510f50b9287946f1a7102ac63eee69b899c1c27a9d3f9f7758f063ddfe329dc16d1998ceb6ae31cf743720b09c0bcb0ab62599625ea639794f1bed2eda6a3a3d1ae5c1933870f339e62c0478ff132c97992a19afe814b7e3b6533905421ee8107d0261682bbcaf74a206e271317191af148e42da5bf16f9cbd5ad7cc76ed8400c1588a80c0cca9648593fd2316d632ebbb9a911967be1b4d6739c04fd14e26ebfdd9ae666dd8984cd3021e3b9cf7c984f22de698784ed0298f4408ae11eedc561c91615f2f73a2716f7b05db1c27fcedd7db795af9ea6fb0b75ba40ff33549601028f700ab7e2d873dd07c95d566b2b04e163f33d5522ffe223d0d39d81e57fda94d7dda6abeb658dc23f54034e584e6f1059cfe0f6122984320d3598392ff71c27c1406d483a846403fc8e4cd26c14610be472a8cd3395e939261afd896ad3e6f87d5c50224ff4588ee9de4b4abfa7aeecbda6bafaa85dbfdebb2926452565e6d0a19b74fa1bc9ddfa6e066643651d7d9192dbe40aa9529be31c33a3f1edad7901bfc5464f8ed9249358b3582b592f7943e47f28699760416fc6856d0327e63304624c94cc5639aab9126e1404d880dae5394e827e0ab1cf3ce34c4d8d9ac9187bf320066de6da5af87e066f1129c4da55ab5ad4436a90ff5faddaff88aefd4ef294eeb3bbe9a88e59c3784ef97bd6b4b5bd69d7ace9f6f93e0e46df574896ea7f264a70bd43ad94c0239cc2ad38ee93678f949994c9cf53a1d94640f0742e66427b0365b021f32f3a7e5cbfb7d9343535da2d5b9c5bb830aaf143a55cd9285d67e6803601b025a0d9b4b777aa47e50be6162c88c8cd6119374abeab82d3cda5dd18c835e70bb859369f129cfd98adaf2782be1f06ca28ad0e0d64a2fb984b843583d0f85ea68c9fafaeeb6877cb2d56b5bfc7cc98d16aa64e6d9265ec5261de9372b6499a64491d09657844a323f559a64488bc67ab5ca12da6c1259c266fd185a4998dd7b42922e0db3a5daa9a46adcc472da316313bfbbc885f61cacafe6e5f7a29ad4d976ef162c61b9f539ebfa9f46b1282d60a49198c95e841ae109f7f54856ab06bd7f61bad2727b9bd1d5fc766fbc49eee7c158c01e94809bdac7bd47edc249755eeeaea06ac0c7ed230169baecf3047354bc2e0af9094a1dc30bb292e03a7b8da7eed07485ed03659552c86591d95a434af1c013783bb9925b95c926e83ccd4086b1b7c366bfb35b28cc029dcc27112a42ca89b358bed694c53df21edfe505f82f2f255eb428f2444883ca7e34745f43ba6a5a5a5f7de0b37772e2371d6ab3f2b175baf3cd2e8175abb015887e1b11f8fe8f8698dd2dfb2afbf9e81858063c7f4350e0b612a9bf5857c0352c9df64e5036b99aebefb894b6d55eacc47a39354d8ebf53f58c580ae6d880187fb3ca7709b02a90bc0632fbabb698408ab274223d4dafc0065e073cb2404e87d43c206d25a377bb6affd6ee9d2c49acd359265fa3f94353493860323c11b1c6ef79cc26d0aa45488ddb70f97d1aa82ee943ca15a486fe764137ed9078b4861b6f67e59c3cf4d75f535eec61b2b4c63e3245da737c5788329f5425304802b7a573ca1e809c94e38f5dca6c0c0261e8da2804ff445fff6697ea7527a83f58cb3950f82972f57bb41c83f4a622f06ca28d4461cc0d9710e03a72971d25ae5e6cc89faa7325454b0b191fd8467291d8ada48ad0a237ac628c4cb06cb19aafca403bccc41a59b4d67e71dfee90fafbe3a60a54ea7118c99ae2e4c8eb9a4f72534f4f9755b0190ce53e598959baf9460857c448c9c2a12835c42909ef21c06973f204eaa1069d499ddbb99a17c43a7344c74a087422180fcb268c6140b7155b8b242051cc1d5c79e3b71e8b93c09d2b1106377ed62e4be438de743d2f89f250c70864a29c580d021822b3813779ec334909642e2e00be94b1335cefa75be47f0c504b88123b882b3b42b6fda0af1cf891a3f7eafb4bd4ea7ecab2072bc6425fd8175c00d13a4ebe02c93676c65dc20bac58bf1db3ce1e73af5af3147a25372bd5e520c40113c2af080dcd4cf946ed2b58fecaa55c486a58d4c5c56004ff80ccf8a0af14f61bd9be5dee10e38800b38a167b5df7395214eb956bb65cbac7a0e6c04fdb17ef88bbac45269e60a3e1d104296f6287d59d6f1b099346987fdc31f4ec99d0fcacdb8458baaa5109e8d759d32c3041f03c76c6f0b2b64e0a608bad8ac72bfa2749314b2c5d6d713ce744a185c8d6e6d3d627a7afea18cbca633e669869feb0a65a6ecaf792ee06410c85a4d76757561e6d5b90725974b0a2dfc269bc03288987c57c20378deb02b57d27e0c1ad9f4f934607b25c451b1db74878409c9934e171411c2646b281b65a4ac9439e3c63b15b25a83e30fc16483e3f53a5d22210a912e722184700e1ec13df110cc3512c619af98e6e6ce827c086602f1bded2cabb20f62aa5236c6f0702f7a614c061642d05aba806802ba59e2a317f5279581c7c4f2cc78bafe9f14f463621348f2206514c29660c28988b12da607291f51195839fd4f5c21c5f520e5be283d6a3c33e48584140fe39fa682b2e23745c2a356b19c725d53f2297295bfe33f1242ceb006baab34d00dba46a38d9c7e0fe3ef0bb53134faec44a56b4ce34f10036e8db82a5ed4d23b4ff95008c7cc41115dc3ec2ce3aa57e4967804c7e9f9ba8a6428bdd025390a4121c95e79c4ee5f1e2259ad34f1460502a9b120b6a5f16e2a427e9064af3c62ed9dc066a635da749d7102ab77e14d07ce31a8634b031380a5571ea5c2a72f05e30d6ddddde1ed6caca1472262488a62ab76d8ae8df0523094840b0c03dce32f05639b1be41300152410cf635c5b957699b2b21e7daf2bbd142c4db8050b12afcd63134f998824688e9440078eb18c84c43f245e13afcd6306162be1757921ed9622b09c985267d7ae2d1825f446c12aa437bc7b1b262f962ca184124a281a18f37f303e8a6aa68bace40000000049454e44ae426082, '2025-07-16 18:49:28', 'Cardiology'),
('DOC3970', 'paras', 'Mayank2004@', 'Male', 'MBBS', 'mayankdua12104@gmail.com', 0x89504e470d0a1a0a0000000d49484452000000140000000e080400000085cdf47b0000000467414d410000b18f0bfc6105000000206348524d00007a26000080840000fa00000080e8000075300000ea6000003a98000017709cba513c00000002624b474400ff878fccbf000000097048597300002e2300002e230178a53f760000000774494d4507e508130f2e21b4b3cd6e000001564944415428cf5d903d4b9b6114404f42629d4c211914453a044362220ee9e022044129ba74b09353870efd1d9db2994571d5c9a9431705dd44417029d54594861a03a690a190f40de474781f8b7aee74cffd829b105e91a74c85126f9820c328d0a74b9b5b2ef9ce0faef98b45776dda332632b26fdfc828989e4d772d263c60999863ae48f1950818e13d11459642ed30498d47da64290369d2c02c591efed76a097f3246934b2ef8cd1726e8f20b98e235776cf18139005ab8e05b7326c5ba43cf5c356fde55cf1c5a37ef8e7f6cfb091f63c9076f9c77dd238f5c77de1b3bd64cfbce059310b7153c571b966da9dab26c433d77265e9404a0c43655e09e717200e418a70d54d9a6080900273d0d3fdb77da13554f9c763fd853272105642984272cb3c847d6806f2cb2126c812c772940864165d8648f63e0331b8c053b4400acd8f1290307cff28e95f834106662522f7200fe01553aea1cfa9914d40000002574455874646174653a63726561746500323032312d30382d31395431353a34363a33322b30303a30306c3f9d400000002574455874646174653a6d6f6469667900323032312d30382d31395431353a34363a33322b30303a30301d6225fc0000001974455874536f6674776172650041646f626520496d616765526561647971c9653c0000000049454e44ae426082, '2025-07-17 09:24:01', 'Neurology');

-- --------------------------------------------------------

--
-- Table structure for table `doctors`
--

CREATE TABLE `doctors` (
  `doctor_id` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `doctor_availability`
--

CREATE TABLE `doctor_availability` (
  `availability_id` int(11) NOT NULL,
  `doctor_id` varchar(30) NOT NULL,
  `available_day` varchar(20) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `doctor_details`
--

CREATE TABLE `doctor_details` (
  `id` int(10) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `mobile` varchar(10) DEFAULT NULL,
  `address` varchar(32) DEFAULT NULL,
  `qualification` varchar(32) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `bloodgroup` varchar(32) DEFAULT NULL,
  `dateofjoining` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `medical_records`
--

CREATE TABLE `medical_records` (
  `record_id` int(11) NOT NULL,
  `patient_id` varchar(50) NOT NULL,
  `doctor_id` varchar(50) NOT NULL,
  `diagnosis` varchar(255) NOT NULL,
  `treatment` varchar(255) NOT NULL,
  `notes` text DEFAULT NULL,
  `record_date` date NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medical_records`
--

INSERT INTO `medical_records` (`record_id`, `patient_id`, `doctor_id`, `diagnosis`, `treatment`, `notes`, `record_date`, `created_at`) VALUES
(12, 'PAT97125', 'DOC2420', 'check up', 'asaadda', 'fdfdffdffffsfsfsffsf', '2025-07-17', '2025-07-17 09:36:11');

-- --------------------------------------------------------

--
-- Table structure for table `medicines`
--

CREATE TABLE `medicines` (
  `medicine_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `brand` varchar(100) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `expiry_date` date NOT NULL,
  `added_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `medicines`
--

INSERT INTO `medicines` (`medicine_id`, `name`, `brand`, `quantity`, `price`, `expiry_date`, `added_at`) VALUES
(2, 'Paracetamol', 'cotax', 100, 3.59, '2025-07-01', '2025-07-10 07:00:55'),
(3, 'ibrufene', 'isotex', 5, 10.00, '2025-07-31', '2025-07-10 19:27:10'),
(4, 'paracetamol', 'xuyg', 10, 100.00, '2025-08-19', '2025-07-16 19:10:31'),
(5, 'cotax', 'cotax345', 3, 89.00, '2025-07-02', '2025-07-16 19:11:45'),
(6, 'cudp100', 'cudp', 910, 3.00, '2025-08-19', '2025-07-17 06:40:36');

-- --------------------------------------------------------

--
-- Table structure for table `patient`
--

CREATE TABLE `patient` (
  `ID` int(11) NOT NULL,
  `Name` varchar(30) DEFAULT NULL,
  `Age` int(11) DEFAULT NULL,
  `Gender` varchar(6) DEFAULT NULL,
  `Address` varchar(50) DEFAULT NULL,
  `District` varchar(25) DEFAULT NULL,
  `Ph_no` varchar(10) DEFAULT NULL,
  `Bloodgrp` varchar(5) DEFAULT NULL,
  `Appointment` date DEFAULT NULL,
  `Symptoms` varchar(100) DEFAULT NULL,
  `Doctor` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `patients`
--

CREATE TABLE `patients` (
  `patient_id` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `blood_group` varchar(5) DEFAULT NULL,
  `contact` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `aadhaar` varchar(20) DEFAULT NULL,
  `guardian` varchar(100) DEFAULT NULL,
  `insurance` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `patients`
--

INSERT INTO `patients` (`patient_id`, `name`, `gender`, `age`, `dob`, `blood_group`, `contact`, `email`, `address`, `aadhaar`, `guardian`, `insurance`, `image_path`, `password`) VALUES
('PAT97125', 'abcd', 'Male', 19, '2020-09-09', 'A+', '7567502600', 'mayankdua12104@gmail.com', 'Tarapur', '123412341234', 'sheetal', 'sbin789', 'D:\\MCA\\netbeans\\hospital-management-system-main\\img\\diagnosis\\logo.png', 'Mayank2004@');

-- --------------------------------------------------------

--
-- Table structure for table `patient_info`
--

CREATE TABLE `patient_info` (
  `patient_id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `age` int(11) NOT NULL,
  `dob` date NOT NULL,
  `blood_group` varchar(5) DEFAULT NULL,
  `contact` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `address` text DEFAULT NULL,
  `aadhaar` varchar(20) NOT NULL,
  `guardian` varchar(100) DEFAULT NULL,
  `insurance` varchar(100) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pharmacy`
--

CREATE TABLE `pharmacy` (
  `billid` int(11) NOT NULL,
  `id` int(11) NOT NULL,
  `bill` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pharmacy_bills`
--

CREATE TABLE `pharmacy_bills` (
  `bill_id` int(11) NOT NULL,
  `prescription_id` int(11) DEFAULT NULL,
  `patient_id` varchar(50) DEFAULT NULL,
  `doctor_id` varchar(50) DEFAULT NULL,
  `bill_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pharmacy_bill_items`
--

CREATE TABLE `pharmacy_bill_items` (
  `bill_item_id` int(11) NOT NULL,
  `bill_id` int(11) DEFAULT NULL,
  `medicine_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pharmacy_prescription_management`
--

CREATE TABLE `pharmacy_prescription_management` (
  `record_id` int(11) NOT NULL,
  `prescription_id` int(11) NOT NULL,
  `pharmacist_id` varchar(50) NOT NULL,
  `dispense_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pharmacy_users`
--

CREATE TABLE `pharmacy_users` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pharmacy_users`
--

INSERT INTO `pharmacy_users` (`id`, `name`, `email`, `password`, `created_at`) VALUES
(1, '', 'aroramayank488@gmail.com', 'Mayank2004@', '2025-06-19 12:03:06');

-- --------------------------------------------------------

--
-- Table structure for table `prescriptions`
--

CREATE TABLE `prescriptions` (
  `prescription_id` int(11) NOT NULL,
  `appointment_id` int(11) DEFAULT NULL,
  `patient_id` varchar(50) DEFAULT NULL,
  `doctor_id` varchar(50) DEFAULT NULL,
  `medicine_id` int(11) DEFAULT NULL,
  `prescription_date` date DEFAULT NULL,
  `dosage` varchar(100) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prescriptions`
--

INSERT INTO `prescriptions` (`prescription_id`, `appointment_id`, `patient_id`, `doctor_id`, `medicine_id`, `prescription_date`, `dosage`, `quantity`, `status`) VALUES
(25, 16, 'PAT97125', 'DOC2420', 3, '2025-07-17', '1-0-1', 305, 'Dispensed'),
(26, 16, 'PAT97125', 'DOC2420', 6, '2025-07-17', '1-1-1', 90, 'Dispensed');

-- --------------------------------------------------------

--
-- Table structure for table `prescription_items`
--

CREATE TABLE `prescription_items` (
  `item_id` int(11) NOT NULL,
  `prescription_id` int(11) DEFAULT NULL,
  `medicine_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `dosage` varchar(100) DEFAULT NULL,
  `duration` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `room`
--

CREATE TABLE `room` (
  `RoomID` int(11) NOT NULL,
  `roomtype` varchar(32) DEFAULT NULL,
  `patientname` varchar(32) DEFAULT NULL,
  `ad_date` varchar(32) DEFAULT NULL,
  `ID` int(11) NOT NULL,
  `MedID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `failed_attempts` int(11) DEFAULT 0,
  `lock_time` datetime DEFAULT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_token_expiry` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `failed_attempts`, `lock_time`, `reset_token`, `reset_token_expiry`) VALUES
(1, 'Mayank', 'aroramayank488@gmail.com', 'MayankDua@1', 0, NULL, NULL, NULL),
(2, 'Meet', 'mayankdua12104@gmail.com', 'MayankDua@75', 0, NULL, NULL, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin_users`
--
ALTER TABLE `admin_users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `appointments`
--
ALTER TABLE `appointments`
  ADD PRIMARY KEY (`appointment_id`),
  ADD KEY `fk_patient` (`patient_id`),
  ADD KEY `fk_doctor` (`doctor_id`);

--
-- Indexes for table `bill_log`
--
ALTER TABLE `bill_log`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `diagnosis`
--
ALTER TABLE `diagnosis`
  ADD PRIMARY KEY (`MedID`),
  ADD KEY `ID` (`ID`);

--
-- Indexes for table `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`doctor_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `doctors`
--
ALTER TABLE `doctors`
  ADD PRIMARY KEY (`doctor_id`);

--
-- Indexes for table `doctor_availability`
--
ALTER TABLE `doctor_availability`
  ADD PRIMARY KEY (`availability_id`),
  ADD KEY `doctor_id` (`doctor_id`);

--
-- Indexes for table `doctor_details`
--
ALTER TABLE `doctor_details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `medical_records`
--
ALTER TABLE `medical_records`
  ADD PRIMARY KEY (`record_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `doctor_id` (`doctor_id`);

--
-- Indexes for table `medicines`
--
ALTER TABLE `medicines`
  ADD PRIMARY KEY (`medicine_id`);

--
-- Indexes for table `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `patients`
--
ALTER TABLE `patients`
  ADD PRIMARY KEY (`patient_id`);

--
-- Indexes for table `patient_info`
--
ALTER TABLE `patient_info`
  ADD PRIMARY KEY (`patient_id`);

--
-- Indexes for table `pharmacy`
--
ALTER TABLE `pharmacy`
  ADD PRIMARY KEY (`billid`),
  ADD KEY `id` (`id`);

--
-- Indexes for table `pharmacy_bills`
--
ALTER TABLE `pharmacy_bills`
  ADD PRIMARY KEY (`bill_id`),
  ADD KEY `prescription_id` (`prescription_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `doctor_id` (`doctor_id`);

--
-- Indexes for table `pharmacy_bill_items`
--
ALTER TABLE `pharmacy_bill_items`
  ADD PRIMARY KEY (`bill_item_id`),
  ADD KEY `bill_id` (`bill_id`),
  ADD KEY `medicine_id` (`medicine_id`);

--
-- Indexes for table `pharmacy_prescription_management`
--
ALTER TABLE `pharmacy_prescription_management`
  ADD PRIMARY KEY (`record_id`),
  ADD KEY `prescription_id` (`prescription_id`);

--
-- Indexes for table `pharmacy_users`
--
ALTER TABLE `pharmacy_users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `prescriptions`
--
ALTER TABLE `prescriptions`
  ADD PRIMARY KEY (`prescription_id`),
  ADD KEY `appointment_id` (`appointment_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `doctor_id` (`doctor_id`),
  ADD KEY `medicine_id` (`medicine_id`);

--
-- Indexes for table `prescription_items`
--
ALTER TABLE `prescription_items`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `prescription_id` (`prescription_id`),
  ADD KEY `medicine_id` (`medicine_id`);

--
-- Indexes for table `room`
--
ALTER TABLE `room`
  ADD KEY `MedID` (`MedID`),
  ADD KEY `ID` (`ID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin_users`
--
ALTER TABLE `admin_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `appointments`
--
ALTER TABLE `appointments`
  MODIFY `appointment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `bill_log`
--
ALTER TABLE `bill_log`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `doctor_availability`
--
ALTER TABLE `doctor_availability`
  MODIFY `availability_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `medical_records`
--
ALTER TABLE `medical_records`
  MODIFY `record_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `medicines`
--
ALTER TABLE `medicines`
  MODIFY `medicine_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `pharmacy_bills`
--
ALTER TABLE `pharmacy_bills`
  MODIFY `bill_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pharmacy_bill_items`
--
ALTER TABLE `pharmacy_bill_items`
  MODIFY `bill_item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pharmacy_prescription_management`
--
ALTER TABLE `pharmacy_prescription_management`
  MODIFY `record_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pharmacy_users`
--
ALTER TABLE `pharmacy_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `prescriptions`
--
ALTER TABLE `prescriptions`
  MODIFY `prescription_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- AUTO_INCREMENT for table `prescription_items`
--
ALTER TABLE `prescription_items`
  MODIFY `item_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `fk_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_patient` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `diagnosis`
--
ALTER TABLE `diagnosis`
  ADD CONSTRAINT `diagnosis_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `patient` (`ID`);

--
-- Constraints for table `doctor_availability`
--
ALTER TABLE `doctor_availability`
  ADD CONSTRAINT `doctor_availability_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`);

--
-- Constraints for table `medical_records`
--
ALTER TABLE `medical_records`
  ADD CONSTRAINT `medical_records_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `medical_records_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`) ON DELETE CASCADE;

--
-- Constraints for table `pharmacy`
--
ALTER TABLE `pharmacy`
  ADD CONSTRAINT `pharmacy_ibfk_1` FOREIGN KEY (`id`) REFERENCES `patient` (`ID`);

--
-- Constraints for table `pharmacy_bills`
--
ALTER TABLE `pharmacy_bills`
  ADD CONSTRAINT `pharmacy_bills_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`),
  ADD CONSTRAINT `pharmacy_bills_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  ADD CONSTRAINT `pharmacy_bills_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`);

--
-- Constraints for table `pharmacy_bill_items`
--
ALTER TABLE `pharmacy_bill_items`
  ADD CONSTRAINT `pharmacy_bill_items_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `pharmacy_bills` (`bill_id`),
  ADD CONSTRAINT `pharmacy_bill_items_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`);

--
-- Constraints for table `pharmacy_prescription_management`
--
ALTER TABLE `pharmacy_prescription_management`
  ADD CONSTRAINT `pharmacy_prescription_management_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`);

--
-- Constraints for table `prescriptions`
--
ALTER TABLE `prescriptions`
  ADD CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`appointment_id`),
  ADD CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  ADD CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`doctor_id`),
  ADD CONSTRAINT `prescriptions_ibfk_4` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`);

--
-- Constraints for table `prescription_items`
--
ALTER TABLE `prescription_items`
  ADD CONSTRAINT `prescription_items_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`),
  ADD CONSTRAINT `prescription_items_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`);

--
-- Constraints for table `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `room_ibfk_1` FOREIGN KEY (`MedID`) REFERENCES `diagnosis` (`MedID`),
  ADD CONSTRAINT `room_ibfk_2` FOREIGN KEY (`ID`) REFERENCES `patient` (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
