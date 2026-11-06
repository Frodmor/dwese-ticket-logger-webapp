package org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.controllers;

import jakarta.validation.Valid;
import org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.DAO.RegionDAO;
import org.alixar.daw2.FelixRodriguez.dwese_ticket_logger_webapp.entities.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataAccessException;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/regions")
public class RegionController {
    private static final Logger logger =
            LoggerFactory.getLogger(RegionController.class);
    // DAO para gestionar las operaciones de las regiones en la base de datos
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RegionDAO regionDAO;

    @GetMapping
    public String listRegions(Model model) {
        logger.info("Solicitando la lista de todas las regiones...");
        List<Region> listRegions = null;
        try {
            listRegions = regionDAO.ListAllRegion();
            logger.info("Se han cargado {} regiones.", listRegions.size());
        } catch (DataAccessException | SQLException e) {
            logger.error("Error al listar las regiones: {}", e.getMessage());
            model.addAttribute("errorMessage", "Error al listar las regiones.");
        }
        model.addAttribute("listRegions", listRegions); // Pasar la lista deregiones al modelo
        return "region"; // Nombre de la plantilla Thymeleaf a renderizar
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nueva región.");
        model.addAttribute("region", new Region()); // Crear un nuevo objeto Region
        return "region-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para la región con ID {}",
                id);
        Region region = null;
        try {
            region = regionDAO.obtenerPorId(id);
            if (region == null) {
                logger.warn("No se encontró la región con ID {}", id);
            }
        } catch (DataAccessException | SQLException e) {
            logger.error("Error al obtener la región con ID {}: {}", id,
                    e.getMessage());
            model.addAttribute("errorMessage", "Error al obtener la región.");
        }
        model.addAttribute("region", region);
        return "region-form"; // Nombre de la plantilla Thymeleaf para el formulario
    }

    @PostMapping("/insert")
    public String insertRegion(@Valid @ModelAttribute("region") Region region,
                               BindingResult result, RedirectAttributes redirectAttributes) {
        logger.info("Insertando nueva región con código {}", region.getCode());
        try {
            if (result.hasErrors()) {
                return "region-form"; // Devuelve el formulario para mostrar los errores de validación
            }
            if (regionDAO.existePorCodigo(region.getCode())) {
                logger.warn("El código de la región {} ya existe.", region.getCode());
                redirectAttributes.addFlashAttribute("errorMessage", "El código de la región ya existe.");
                return "redirect:/regions/new";
            }
            regionDAO.insertarRegion(region);
            logger.info("Región {} insertada con éxito.", region.getCode());
        } catch (DataAccessException e) {
            logger.error("Error al insertar la región {}: {}", region.getCode(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al insertar la región.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }

    @PostMapping("/update")
    public String updateRegion(@Valid @ModelAttribute("region") Region region, BindingResult result, RedirectAttributes redirectAttributes) {
        logger.info("Actualizando región con ID {}", region.getId());
        try {
            if (result.hasErrors()) {
                return "region-form"; // Devuelve el formulario para mostrar los errores de validación
            }
            if (regionDAO.existePorCodigoNoPorId(region.getCode(), region.getId())) {
                logger.warn("El código de la región {} ya existe para otra región.",
                        region.getCode());
                redirectAttributes.addFlashAttribute("errorMessage", "El código de la región ya existe para otra región.");
                return "redirect:/regions/edit?id=" + region.getId();
            }
            regionDAO.actualizarRegion(region);
            logger.info("Región con ID {} actualizada con éxito.", region.getId());
        } catch (DataAccessException e) {
            logger.error("Error al actualizar la región con ID {}: {}",
                    region.getId(), e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar la región.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }


    @PostMapping("/delete")
    public String deleteRegion(@RequestParam("id") Long id, RedirectAttributes
            redirectAttributes) {
        logger.info("Eliminando región con ID {}", id);
        try {
            regionDAO.borrarRegion(id);
            logger.info("Región con ID {} eliminada con éxito.", id);
        } catch (DataAccessException | SQLException e) {
            logger.error("Error al eliminar la región con ID {}: {}", id,
                    e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar la región.");
        }
        return "redirect:/regions"; // Redirigir a la lista de regiones
    }
}
