package org.sid.web;

import javax.validation.Valid;
import org.sid.DAO.ProduitRepository;
import org.sid.entities.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProduitController {
	
	@Autowired
	ProduitRepository produitRepository;
	
	@RequestMapping(value="/index")
	public String index(Model model, @RequestParam(name="page", defaultValue="0") int p,
			@RequestParam(name="size", defaultValue="5") int s,
			@RequestParam(name="mc", defaultValue="") String mc){
		Page<Produit> pageProduits = produitRepository.chercher("%"+mc+"%", new PageRequest(p, s));
		model.addAttribute("listProduits", pageProduits.getContent());
		int [] pages = new int[pageProduits.getTotalPages()];
		model.addAttribute("pages", pages);
		model.addAttribute("size", s);
		model.addAttribute("pageCourante", p);
		model.addAttribute("mc", mc);
		
		return "produits";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	public String delete(Long id, String mc, int page, int size){
		produitRepository.delete(id);
		return "redirect:/index?page="+page+"&size="+size+"&mc="+mc+"";
		
		}
	
	@RequestMapping(value="/form", method=RequestMethod.GET)
	public String formProduit(Model model){
		model.addAttribute("produit", new Produit());
		return "formProduit";
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public String formProduit(Model model, Long id){
		Produit p= produitRepository.findOne(id);
		model.addAttribute("produit", p);
		return "editProduit";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public String save(Model model, @Valid Produit produit, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			return "formProduit";
		else{
		produitRepository.save(produit);
		return "redirect:/index";}
	}

}
