-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-10-2022 a las 10:23:05
-- Versión del servidor: 10.4.22-MariaDB
-- Versión de PHP: 7.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `coco_chat`
--

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `amigos`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `amigos` (
`idUser` int(11)
,`name` varchar(50)
,`nickname` varchar(50)
,`idUser1` int(11)
);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `friend`
--

CREATE TABLE `friend` (
  `nickname` varchar(50) NOT NULL,
  `idUser1` int(11) NOT NULL,
  `idUser2` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `groups`
--

CREATE TABLE `groups` (
  `nameGroup` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `messagesfriends`
--

CREATE TABLE `messagesfriends` (
  `message` varchar(280) NOT NULL,
  `timeStamp` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `idEmiter` int(11) NOT NULL,
  `idReceptor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `messagesfriends`
--

INSERT INTO `messagesfriends` (`message`, `timeStamp`, `idEmiter`, `idReceptor`) VALUES
('123', '2022-10-26 12:40:30.000000', 2, 1),
('AGUAYO', '2022-10-26 12:40:43.000000', 2, 1),
('HOLA', '2022-10-26 12:41:12.000000', 2, 4),
('123123', '2022-10-26 12:48:24.000000', 2, 1),
('12315445', '2022-10-26 13:21:12.000000', 1, 1),
('Pilineitor', '2022-10-26 13:21:41.000000', 2, 1),
('JAJAJA', '2022-10-26 13:21:52.000000', 1, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `messagesgroups`
--

CREATE TABLE `messagesgroups` (
  `nombre` varchar(50) NOT NULL,
  `timeStamp` timestamp(6) NOT NULL DEFAULT current_timestamp(6) ON UPDATE current_timestamp(6),
  `mensaje` varchar(280) NOT NULL,
  `emitter` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `conected` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`idUser`, `name`, `email`, `password`, `conected`) VALUES
(1, 'test1', 'test1', 'test1', 1),
(2, '123', '123', '123', 1),
(3, '1234', '1234', '1234', 1),
(4, 'alexis', 'alexis', 'alexis', 1),
(5, '123456', '123456', '123456', 1),
(6, 'ivan', 'ivan', 'ivan', 1),
(7, 'alonso', 'alonso', 'alonso', 1);

-- --------------------------------------------------------

--
-- Estructura para la vista `amigos`
--
DROP TABLE IF EXISTS `amigos`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `amigos`  AS SELECT `user`.`idUser` AS `idUser`, `user`.`name` AS `name`, `friend`.`nickname` AS `nickname`, `friend`.`idUser1` AS `idUser1` FROM (`user` join `friend`) WHERE `user`.`idUser` = `friend`.`idUser2` ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `friend`
--
ALTER TABLE `friend`
  ADD KEY `idUser1` (`idUser1`),
  ADD KEY `idUser2` (`idUser2`);

--
-- Indices de la tabla `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`nameGroup`(50));

--
-- Indices de la tabla `messagesfriends`
--
ALTER TABLE `messagesfriends`
  ADD KEY `idEmiter` (`idEmiter`),
  ADD KEY `idReceptor` (`idReceptor`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `friend`
--
ALTER TABLE `friend`
  ADD CONSTRAINT `friend_ibfk_1` FOREIGN KEY (`idUser1`) REFERENCES `user` (`idUser`),
  ADD CONSTRAINT `friend_ibfk_2` FOREIGN KEY (`idUser2`) REFERENCES `user` (`idUser`);

--
-- Filtros para la tabla `messagesfriends`
--
ALTER TABLE `messagesfriends`
  ADD CONSTRAINT `messagesfriends_ibfk_1` FOREIGN KEY (`idEmiter`) REFERENCES `user` (`idUser`),
  ADD CONSTRAINT `messagesfriends_ibfk_2` FOREIGN KEY (`idReceptor`) REFERENCES `user` (`idUser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
