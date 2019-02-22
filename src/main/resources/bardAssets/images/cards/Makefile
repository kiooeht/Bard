PROD?=../../../../../../productionAssets/cards
ORIG_IMAGES:=$(shell find $(PROD) -type f -name '*.png')
IMAGES:=$(patsubst $(PROD)/%.png, %.png, $(ORIG_IMAGES))
BIG_IMAGES:=$(patsubst $(PROD)/%.png, %_p.png, $(ORIG_IMAGES))

.PHONY: all verify clean

all: $(BIG_IMAGES) $(IMAGES)

attack/%_p.png: $(PROD)/attack/%.png
	@mkdir -p $(@D)
	cp $< $@

skill/%_p.png: $(PROD)/skill/%.png
	@mkdir -p $(@D)
	cp $< $@

power/%_p.png: $(PROD)/power/%.png
	@mkdir -p $(@D)
	cp $< $@

attack/%.png: $(PROD)/attack/%.png
	@mkdir -p $(@D)
	convert $< -resize 50% $@

skill/%.png: $(PROD)/skill/%.png
	@mkdir -p $(@D)
	convert $< -resize 50% $@

power/%.png: $(PROD)/power/%.png
	@mkdir -p $(@D)
	convert $< -resize 50% $@

verify:
	@echo $(ORIG_IMAGES)
	@echo $(IMAGES)
	@echo $(BIG_IMAGES)

clean:
	-@rm -r attack
	-@rm -r skill
	-@rm -r power
