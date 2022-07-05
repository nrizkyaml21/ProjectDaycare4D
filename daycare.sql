-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 05, 2022 at 04:45 AM
-- Server version: 10.1.38-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `daycare`
--

-- --------------------------------------------------------

--
-- Table structure for table `anak`
--

CREATE TABLE `anak` (
  `id` int(10) NOT NULL,
  `nama` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `anak`
--

INSERT INTO `anak` (`id`, `nama`) VALUES
(201001, 'Qia Nadira'),
(201002, 'Starla Xyva'),
(201003, 'Erisca Shayle'),
(201004, 'Jason Aryawinata'),
(201005, 'Cherry Sabrina'),
(201006, 'Phrince Gabriel'),
(201007, 'Aaron Alfreza');

-- --------------------------------------------------------

--
-- Table structure for table `data_penitipan`
--

CREATE TABLE `data_penitipan` (
  `id` int(10) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `anak_id` int(11) DEFAULT NULL,
  `usia` varchar(255) DEFAULT NULL,
  `status` enum('Bayi','Balita','Anak','') DEFAULT NULL,
  `jenis_kelamin` enum('Laki laki','Perempuan') DEFAULT NULL,
  `tanggal_penitipan` date DEFAULT NULL,
  `waktu` varchar(255) DEFAULT NULL,
  `harga` int(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `data_penitipan`
--

INSERT INTO `data_penitipan` (`id`, `nama`, `anak_id`, `usia`, `status`, `jenis_kelamin`, `tanggal_penitipan`, `waktu`, `harga`) VALUES
(201002, 'Arash Zachary', 201002, '4 Tahun', 'Balita', 'Perempuan', '2022-07-01', '5 jam', 500000),
(201003, 'Rigel Angkasa', 201001, '1 Tahun', 'Balita', 'Perempuan', '2022-07-13', '3 Jam', 300000),
(201004, 'Magnolia Safira', 201003, '5 Tahun', 'Anak', 'Perempuan', '2022-06-22', '6 Jam', 600000),
(201005, 'Fracelia Freyya', 201004, '3 Bulan', 'Bayi', 'Laki laki', '2022-07-02', '2 jam', 200000),
(201006, 'Arka  Adison', 201007, '8 Bulan', 'Bayi', 'Laki laki', '2022-06-30', '1 Jam', 100000),
(201007, 'Rania Aleca', 201005, '3 Tahun', 'Balita', 'Laki laki', '2022-06-28', '3 Jam', 300000);

-- --------------------------------------------------------

--
-- Table structure for table `orang_tua`
--

CREATE TABLE `orang_tua` (
  `no_member` int(11) NOT NULL,
  `nama` varchar(60) NOT NULL,
  `no_telepon` varchar(12) NOT NULL,
  `alamat` varchar(60) NOT NULL,
  `nama_anak` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pegawai`
--

CREATE TABLE `pegawai` (
  `id` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `no_telepon` varchar(12) NOT NULL,
  `email` varchar(50) NOT NULL,
  `alamat` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `anak`
--
ALTER TABLE `anak`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `data_penitipan`
--
ALTER TABLE `data_penitipan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pegawai`
--
ALTER TABLE `pegawai`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `anak`
--
ALTER TABLE `anak`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=201008;

--
-- AUTO_INCREMENT for table `data_penitipan`
--
ALTER TABLE `data_penitipan`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=201011;

--
-- AUTO_INCREMENT for table `pegawai`
--
ALTER TABLE `pegawai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
