
singleton TSShapeConstructor(Kelp_01Dae)
{
   baseShape = "./Kelp_01.dae";
};

function Kelp_01Dae::onLoad(%this)
{
   %this.addImposter("64", "8", "0", "0", "512", "1", "0");
}
